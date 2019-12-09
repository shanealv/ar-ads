# ar-ads

An app for displaying AR based ads for large format displays running android.

## Getting Started

For this project, you'll want to use [android studio][an_studio] with the
[sceneform plugin][sf_plugin] installed. Also, be sure you have general
understanding of [ARCore][ar_core] before starting.

If you are doing any asset development, you'll also want an up-to-date versions
of [Blender][blender], [gimp][gimp], and [Unity][unity]. Assets included in this
project include the bear-face (custom) and fox-face (from the ARCore samples)
assets. Asset development will not be covered in this guide.

This project also makes use of [Git LFS][git_lfs] for all binary assests. You'll
want to make sure it's installed before working with this project. This will
ensure assets are pulled correctly. **Make sure to do this before cloning**.

Optionally, you can also install [git-flow][git_flow] for branch management and
[Commitizen][commitizen] (requires Pyhon 3.6+) for commit message formatting. If
you are working on documentation, I strongly suggest reformatting documents with
[Prettier][prettier] before committing.

To clone this project, use the following command in your terminal:

```sh
git clone git@github.com:shanealv/ar-ads.git
```

From there, it's a standard Android project. Opening up the folder in Android
Studio will ensure most of the setup is handled for you. From there, building
and running on an recent android device is mostly trivial. **You may encounter
issues running this on an emulator**. If you must run on an emulator, you'll
need to refer to the minimum requirements for ARCore for how to set things up
correctly.

[an_studio]: https://developer.android.com/studio
[ar_core]: https://developers.google.com/ar/develop/java/quickstart
[blender]: https://www.blender.org/
[commitizen]: https://github.com/woile/commitizen
[gimp]: https://www.gimp.org/
[git_flow]: https://github.com/nvie/gitflow
[git_lfs]: https://git-lfs.github.com/
[prettier]: https://prettier.io/
[sf_plugin]: https://developers.google.com/ar/develop/java/sceneform/#import-sceneform-plugin
[unity]: https://unity.com/
