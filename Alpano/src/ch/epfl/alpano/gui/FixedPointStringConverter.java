package ch.epfl.alpano.gui;
import java.math.BigDecimal;

import javafx.util.StringConverter;
import java.math.RoundingMode;
/**
 * convertit entre chaînes de caractères et sa représentation en flotant avec une précision déterminée dans le constructeur.
 * Si la présision est de zero, il convertit entre chaînes de caractères et son entier correspondant
 * @author Paul-Louis DELACOUR
 * @author Luc MICHELS
 */
public final class FixedPointStringConverter extends StringConverter<Integer>{

    private final int decimals;
   
    /**
     * Créer un convertiseur chaînes de caractères et sa représentation en flotant avec une précision déterminer par "decimals".
     * Si la présision est de zéro il convertit entre chaînes de caractères et son entier correspondant
     * @param decimals le nombres de décimales qui représente la précision du flotant
     */
    public FixedPointStringConverter(int decimals) {
        this.decimals=decimals;
    }
    
    @Override
    public Integer fromString(String string) {
        return new BigDecimal(string)
                    .setScale(decimals, RoundingMode.HALF_UP)
                    .stripTrailingZeros()
                    .movePointRight(decimals)
                    .intValueExact();
    }

    @Override
    public String toString(Integer object) {
        
        return object == null ? "" : new BigDecimal(object)
                    .movePointLeft(decimals)
                    .stripTrailingZeros()
                    .toPlainString();
    }

}
