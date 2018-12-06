package ch.epfl.alpano.gui;

public interface PredefinedPanoramas {

    public static int WIDTH = 2500; // Largeur commune à tous les panoramas
    public static int HEIGHT = 800; // Hauteur commune à tous les panoramas
    public static int MAX_DISTANCE = 300; // Distance maximale commun à tous les panoramas en km
    public static int SUPER_SAMPLING_EXPONENT = 0;
    
    /**
     * les paramètres représentant Niesen
     */
    public static PanoramaUserParameters NIESEN = new PanoramaUserParameters(
            76_500,
            467_300, 
            600, 
            180, 
            110, 
            MAX_DISTANCE, 
            WIDTH, 
            HEIGHT, 
            SUPER_SAMPLING_EXPONENT);
    
    /**
     * les paramètres représentant les Alpes du Jura
     */
    public static PanoramaUserParameters ALPES_DU_JURA = new PanoramaUserParameters(
            68_087,
            470_085, 
            1380, 
            162, 
            27, 
            MAX_DISTANCE, 
            WIDTH, 
            HEIGHT, 
            SUPER_SAMPLING_EXPONENT);
    
    /**
     * les paramètres représentant le Mont Racine
     */
    public static PanoramaUserParameters MONT_RACINE = new PanoramaUserParameters(
            68_200,
            470_200, 
            1500, 
            135, 
            45, 
            MAX_DISTANCE, 
            WIDTH, 
            HEIGHT, 
            SUPER_SAMPLING_EXPONENT);
    
    /**
     * les paramètres représentant Finsteraarhorn
     */
    public static PanoramaUserParameters FINSTERAARHORN = new PanoramaUserParameters(
            81_260,
            465_374, 
            4_300, 
            205, 
            20, 
            MAX_DISTANCE, 
            WIDTH, 
            HEIGHT, 
            SUPER_SAMPLING_EXPONENT);
    
    /**
     * les paramètres représentant la Tour de Sauvabelin
     */
    public static PanoramaUserParameters TOUR_DE_SAUVABELIN = new PanoramaUserParameters(
            66_385,
            465_353, 
            700, 
            135, 
            100, 
            MAX_DISTANCE, 
            WIDTH, 
            HEIGHT, 
            SUPER_SAMPLING_EXPONENT);
    
    /**
     * les paramètres représentant la Plage du Pelican
     */
    public static PanoramaUserParameters PLAGE_DU_PELICAN = new PanoramaUserParameters(
            65_728,
            465_132, 
            380, 
            135, 
            60, 
            MAX_DISTANCE, 
            WIDTH, 
            HEIGHT, 
            SUPER_SAMPLING_EXPONENT);
}
