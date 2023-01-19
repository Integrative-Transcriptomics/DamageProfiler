package org.damageprofiler.services;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public final class ImageUtils {

    public static ImageView loadLogo(){
        final InputStream input = ImageUtils.class.getClassLoader().getResourceAsStream("logo.png");
        assert input != null;
        final Image image = new Image(input);
        return new ImageView(image);
    }
}
