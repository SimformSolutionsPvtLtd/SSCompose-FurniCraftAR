package com.simform.ssfurnicraftar.data.remote.api

import com.simform.ssfurnicraftar.data.remote.model.DownloadStatus
import com.simform.ssfurnicraftar.di.OKHTTP_DOWNLOAD_CLIENT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.internal.headersContentLength
import okio.buffer
import okio.sink
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Named

/**
 * Size of the chunk to download at a time
 */
private const val DOWNLOAD_CHUNK_SIZE = 2L * 1024

/**
 * Interval b/w progress updates
 */
private const val PROGRESS_INTERVAL = 500

/**
 * Download service that can be used to download file with progress.
 *
 * @property client The [OkHttpClient] used to download file
 */
class DownloadService @Inject constructor(
    @Named(OKHTTP_DOWNLOAD_CLIENT) private val client: OkHttpClient
) {

    /**
     * Download the file present at [url] and save into given [path].
     *
     * @param path The local file path to store downloaded file
     * @param url The url from where the file will be downloaded.
     * @return Flow of the [DownloadStatus] which can be used to collect progress.
     */
    suspend fun download(path: Path, url: String): Flow<DownloadStatus> = flow {
        emit(DownloadStatus.Loading)

        try {
            val request = Request.Builder()
                .url(url)
                .build()

            // Get request data
            val response = client.newCall(request).execute()

            // Create sink at local file path
            val sink = path.sink(StandardOpenOption.CREATE).buffer()

            // Get source of the response body
            val source = response.body?.source()
            if (source == null) {
                emit(DownloadStatus.Error("Can't start downloading."))
                return@flow
            }

            // Last time when the progress is updated
            var lastTime = System.currentTimeMillis()
            val totalSize = response.headersContentLength()
            var lastRead: Long
            var totalRead = 0L

            emit(DownloadStatus.Progress(0, totalSize))

            // Continuously read data from buffer of defined chunk size. If the read is -1
            // the download is completed.
            while (source
                    .read(sink.buffer, DOWNLOAD_CHUNK_SIZE)
                    .also { lastRead = it } != -1L
            ) {
                sink.emit()
                totalRead += lastRead

                if (System.currentTimeMillis() - lastTime > PROGRESS_INTERVAL) {
                    emit(DownloadStatus.Progress(totalRead, totalSize))
                    lastTime = System.currentTimeMillis()
                }
            }
            emit(DownloadStatus.Completed)
        } catch (e: Exception) {
            emit(DownloadStatus.Error(e.localizedMessage ?: "Something want's wrong", e))
        }
    }
        .flowOn(Dispatchers.IO)
}
