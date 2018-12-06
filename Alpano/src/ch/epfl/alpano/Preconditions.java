package ch.epfl.alpano;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * permet de savoir si tout les prerequis ont etaient atteints ou leve une exception dans le cas contraire
 */
public interface Preconditions {
    
    /**
     * boolean 
     * s'il est vrai on ajoute un message expliquant l erreur
     */
   public static boolean DEBUG = false;

 /**
 *  lève l'exception IllegalArgumentException si son argument est faux, et ne fait rien sinon.
 * @param b boolean (condition pour ne pas avoir d'erreur)
 * @throws IllegalArgumentException
 */
   public static void checkArgument(boolean b){
       if (!b){
           throw new IllegalArgumentException(); 
       }
   }
   /**
    *  lève l'exception IllegalArgumentException avec le message donné attaché si son argument est faux et @see DEBUG est vrai, et ne fait rien sinon.
    * @param b boolean (condition pour ne pas avoir d'erreur)
    * @throws IllegalArgumentException
    */
   public static void checkArgument(boolean b, String message){
       if (!b){
           throw new IllegalArgumentException (DEBUG ? message : "");
       }
   }
 
   /**
    *  lève l'exception NullPointerException avec le message donné attaché si son argument est faux et @see DEBUG est vrai, et ne fait rien sinon.
    * @param b boolean (condition pour ne pas avoir d'erreur)
    * @throws IllegalArgumentException
    */
   public static void checkNullPointer(boolean b, String message){
       if (!b){
           throw new NullPointerException (DEBUG ? message : "");
       }
   }
   /**
    *  lève l'exception NullPointerException si son argument est faux, et ne fait rien sinon.
    * @param b boolean (condition pour ne pas avoir d'erreur)
    * @throws IllegalArgumentException
    */
   public static void checkNullPointer(boolean b){
       if (!b){
           throw new NullPointerException ();
       }
   }
   
   /**
    *  lève l'exception IndexOutOfBoundsException si son argument est faux, et ne fait rien sinon.
    * @param b boolean (condition pour ne pas avoir d'erreur)
    * @throws IllegalArgumentException
    */
   public static void checkIndexOutOfBoundsException(boolean b){
       if(!b){
           throw new IndexOutOfBoundsException();
       }
   }
   
   /**
    *  lève l'exception IndexOutOfBoundsException avec le message donné attaché si son argument est faux et @see DEBUG est vrai, et ne fait rien sinon.
    * @param b boolean (condition pour ne pas avoir d'erreur)
    * @throws IllegalArgumentException
    */
   public static void checkIndexOutOfBoundsException(boolean b , String message){
       if(!b){
           throw new IndexOutOfBoundsException(DEBUG ? message : "");
       }
   }
   
   /**
    *  lève l'exception IllegalStateException si son argument est faux, et ne fait rien sinon.
    * @param b boolean (condition pour ne pas avoir d'erreur)
    * @throws IllegalArgumentException
    */
   public static void checkIllegalStateException(boolean b){
       if(!b){
           throw new IllegalStateException();
       }
   }
   
   /**
    *  lève l'exception IllegalStateException avec le message donné attaché si son argument est faux et @see DEBUG est vrai, et ne fait rien sinon.
    * @param b boolean (condition pour ne pas avoir d'erreur)
    * @throws IllegalArgumentException
    */
   public static void checkIllegalStateException(boolean b, String message){
       if(!b){
           throw new IllegalStateException(DEBUG ? message : "");
       }
   }
   
   
}
