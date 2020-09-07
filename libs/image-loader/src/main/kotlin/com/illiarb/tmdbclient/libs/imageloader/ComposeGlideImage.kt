package com.illiarb.tmdbclient.libs.imageloader

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.FrameManager
import androidx.compose.runtime.onPreCommit
import androidx.compose.runtime.stateFor
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageAsset
import androidx.compose.ui.graphics.asImageAsset
import androidx.compose.ui.graphics.drawscope.drawCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ContextAmbient
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.foundation.Canvas as ComposeCanvas

@Composable
fun ComposeGlideImage(
  image: Image,
  width: Int,
  height: Int,
  modifier: Modifier = Modifier,
  contentScale: ContentScale = ContentScale.None,
  requestOptions: RequestOptions.() -> RequestOptions = { this },
) {
  val imageAsset = stateFor<ImageAsset?>(null) { null }
  val drawable = stateFor<Drawable?>(null) { null }
  val context = ContextAmbient.current

  onPreCommit(imageAsset) {
    val glide = Glide.with(context)
    var target: CustomTarget<Bitmap>? = null

    val job = CoroutineScope(Dispatchers.Main).launch {
      target = object : CustomTarget<Bitmap>() {
        override fun onLoadCleared(placeholder: Drawable?) {
          imageAsset.value = null
          drawable.value = placeholder
        }

        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
          FrameManager.ensureStarted()
          imageAsset.value = resource.asImageAsset()
        }
      }

      val options = requestOptions(RequestOptions())

      glide
        .asBitmap()
        .load(image.asGlideModel())
        .override(width, height)
        .apply(mapOptions(options))
        .into(target!!)
    }

    onDispose {
      imageAsset.value = null
      drawable.value = null
      glide.clear(target)
      job.cancel()
    }
  }

  val theImage = imageAsset.value
  val theDrawable = drawable.value

  if (theImage != null) {
    Image(
      asset = theImage,
      modifier = modifier,
      contentScale = contentScale
    )
  } else if (theDrawable != null) {
    ComposeCanvas(modifier = Modifier.fillMaxSize() + modifier) {
      drawCanvas { canvas, _ ->
        theDrawable.draw(canvas.nativeCanvas)
      }
    }
  }
}