package be.hogent.jochensnextdinner.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun RecipesScreen() {
    Text(text = "RecipesScreen")
}

//AsyncImage(
//model = ImageRequest.Builder(context = LocalContext.current).data(photo.imgSrc)
//.crossfade(true).build(),
//error = painterResource(R.drawable.ic_broken_image),
//placeholder = painterResource(R.drawable.loading_img),
//contentDescription = stringResource(R.string.mars_photo),
//contentScale = ContentScale.Crop,
//modifier = Modifier.fillMaxWidth()
//)