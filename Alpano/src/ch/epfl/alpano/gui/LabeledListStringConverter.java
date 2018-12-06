package ch.epfl.alpano.gui;

import java.util.Arrays;
import java.util.List;

import javafx.util.StringConverter;

/**
 * convertit entre chaînes de caractères et entiers.
 * @author Paul-Louis DELACOUR
 * @author Luc MICHELS
 */
public final class LabeledListStringConverter extends StringConverter<Integer> {

    private List<String> list;
    
    /**
     * Créer un convertiseur entre chaînes de caractères et entiers.
     * @param args la liste des chaînes de caractères dont l'emplacement dans la liste correspond à son entier
     */
    public LabeledListStringConverter(String... args) {
        
        this.list=Arrays.asList(args);
    }

    @Override
    public String toString(Integer object) {
        return list.get(object);
    }

    @Override
    public Integer fromString(String string) {
        return list.indexOf(string);  
    }

}
