package dto;

import entities.Image;

/**
 *
 * @author Henning
 */
public class ImageDTO {

    private String url;

    public ImageDTO(Image image) {
        this.url = image.getImageURL();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
    
    
}
