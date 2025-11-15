// swift-tools-version: 5.9
import PackageDescription

let package = Package(
    name: "MartinezmanoloaCapacitorStartioAds",
    platforms: [.iOS(.v14)],
    products: [
        .library(
            name: "MartinezmanoloaCapacitorStartioAds",
            targets: ["StartioAdsPlugin"])
    ],
    dependencies: [
        .package(url: "https://github.com/ionic-team/capacitor-swift-pm.git", from: "7.0.0")
    ],
    targets: [
        .target(
            name: "StartioAdsPlugin",
            dependencies: [
                .product(name: "Capacitor", package: "capacitor-swift-pm"),
                .product(name: "Cordova", package: "capacitor-swift-pm")
            ],
            path: "ios/Sources/StartioAdsPlugin"),
        .testTarget(
            name: "StartioAdsPluginTests",
            dependencies: ["StartioAdsPlugin"],
            path: "ios/Tests/StartioAdsPluginTests")
    ]
)