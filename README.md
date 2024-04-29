![Banner]

# :chair: SSCompose-FurniCraftAR :calling:

<div align="center">

[![Platform-badge]][Android]
[![Jetpack Compose-badge]][Android]
[![Augmented Reality-badge]][Android]
[![API-badge]][Android]

[![Release-badge]][Release]
[![License Badge-badge]][license]

</div>

<!-- Description -->
Welcome to our Augmented Reality Furniture App! :tada:
Enter into a virtual showroom where you can browse a vast array of furniture products. Visualize the selected furniture into your real-world surroundings through the magic of Augmented Reality (AR). Users can browse a list of furniture products, select a product to view in AR, customize the color of the model, capture the AR scene with the placed furniture model, and share it with others.

## :framed_picture: Preview

https://github.com/SimformSolutionsPvtLtd/SSCompose-FurniCraftAR/assets/147126103/267bd46c-cf1d-49f2-903a-7747c8b00bdf

## :zap: Features

- Explore an extensive catalog of furniture products
- Visualize furniture models in **Augmented Reality**
- Personalize the color scheme of each furniture model
- Seamlessly capture and share your Augmented Reality scenes featuring placed furniture models
- Use links for quick access to specific models with customized colors
- Enjoy dynamic model animations for enhanced visualization
- Experience uninterrupted usage with offline support, utilizing cached data for seamless access to your favorite furniture pieces

## :dart: Requirements

Device that supports **ARCore**. Checkout _[ARCore Supported Devices]_.

## :arrow_down: Download

You can download the most recent version of SSFurniCraftAR from [GitHub releases][Release].

## :hammer_and_wrench: Tech Stack

- **UI Design:** Jetpack Compose
- **Model Catalog:** [SketchFab API]
    - SketchFab's `Data API` enables the listing of various products, while the `Download API` facilitates model downloads.
- **AR Rendering:** [ARSceneView]
    - ARSceneView utilizes `Google Filament` and `ARCore` for seamless 3D model rendering in AR view.
- **Data Management:** Room Database
- **Pagination:** Integrated Jetpack Paging for efficient data handling
- **Architecture:** Following the principles of the [Android App Architecture] for robust and scalable design

## :technologist: Dev Setup

To utilize SketchFab's download API, follow these steps to get & set your API key:

1. Sign In to your [SketchFab] account or create a new one.
2. Navigate to [SketchFab Password & API] settings.
3. Copy your `API token`.
4. Create a `apikeys.properties` file in project root.
5. Add an entry for the API key as `SKETCHFAB_API_KEY=<YOUR_KEY>`.
6. Build and Run the project.

## :crystal_ball: What's next?

We're thrilled to introduce forthcoming features that will elevate your Augmented Reality experience:

- Visualize multiple furniture models at once to mix and match pieces for your space.
- Record and share videos of your AR setups to capture every detail of your design journey.
- View real size tags of placed furniture in AR View.
- Share your screen in real-time during video calls, such as on WhatsApp.

## :construction: Known issues

The custom colors on certain models may not display correctly.
> The effect of baseColor depends on the nature of the surface, controlled by the metallic property.

For more details visit [Filament/materials#baseColor][Filament baseColor].

## :heart: Find this samples useful?

Support it by joining [stargazers] :star: for this repository.

## :handshake: How to Contribute?

Whether you're helping us fix bugs, improve the docs, or a feature request, we'd love to have you! :muscle: \
Check out our __[Contributing Guide]__ for ideas on contributing.

## :lady_beetle: Bugs and Feedback

For bugs, feature requests, and discussion use [GitHub Issues].

## :rocket: Other Mobile Libraries

Check out our other libraries [Awesome-Mobile-Libraries].

## :jigsaw: Acknowledgement

- A huge shout-out to [Thomas Gorisse] for his incredible work on the [ARSceneView] library.
- Special thanks to [SketchFab] for providing access to their extensive collection of models.

## :balance_scale: License

Distributed under the MIT license. See [LICENSE] for details.

<!-- Reference links -->

[Banner]:                   https://github.com/SimformSolutionsPvtLtd/SSCompose-FurniCraftAR/assets/147126103/a02b7b5a-e793-4201-93b9-2f9a2334f39a

[Android]:                  https://www.android.com/

[ARCore Supported Devices]: https://developers.google.com/ar/devices#google_play_devices

[SketchFab]:                https://sketchfab.com

[SketchFab API]:            https://docs.sketchfab.com/data-api/v3/index.html

[SketchFab Password & API]: https://sketchfab.com/settings/password

[ARSceneView]:              https://docs.sketchfab.com/data-api/v3/index.html

[Android App Architecture]:  https://developer.android.com/topic/architecture

[Release]:                  https://github.com/SimformSolutionsPvtLtd/SSCompose-FurniCraftAR/releases/latest

[Filament baseColor]:       https://google.github.io/filament/Materials.html#materialmodels/litmodel/basecolor

[stargazers]:               https://github.com/SimformSolutionsPvtLtd/SSCompose-FurniCraftAR/stargazers

[Contributing Guide]:       CONTRIBUTING.md

[Github Issues]:            https://github.com/SimformSolutionsPvtLtd/SSCompose-FurniCraftAR/issues

[Awesome-Mobile-Libraries]: https://github.com/SimformSolutionsPvtLtd/Awesome-Mobile-Libraries

[Thomas Gorisse]:           https://github.com/ThomasGorisse

[license]:                  LICENSE

<!-- Badges -->

[Platform-badge]:               https://img.shields.io/badge/Platform-Android-green.svg?logo=Android

[Jetpack Compose-badge]:        https://img.shields.io/badge/Jetpack_Compose-v1.5.11-1c274a.svg?logo=jetpackcompose&logoColor=3ddc84

[API-badge]:                    https://img.shields.io/badge/API-26+-51b055

[Augmented Reality-badge]:      https://img.shields.io/badge/Augmented_Reality-053e55.svg?logo=hackthebox&logoColor=9dec03

[Release-badge]:                https://img.shields.io/github/v/release/SimformSolutionsPvtLtd/SSCompose-FurniCraftAR

[License Badge-badge]:          https://img.shields.io/github/license/SimformSolutionsPvtLtd/SSCompose-FurniCraftAR
