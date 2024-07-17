package jaxb;

import exceptions.GameListingFileException;
import game.listing.GameListing;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

public class JAXBConversion {
    private final static String GENERATED_XJC_CLASSES_PACKAGE = "jaxb.generated";
    public static GameListing XMLtoObjectsConversion(InputStream stream) throws JAXBException, GameListingFileException, IOException {
        
    }
}
