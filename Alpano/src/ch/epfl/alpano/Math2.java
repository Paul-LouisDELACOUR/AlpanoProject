package ch.epfl.alpano;

import java.util.function.DoubleUnaryOperator;
/**
 * 
 * @author Paul-Louis DELACOUR (269625)
 * @author Luc MICHELS (273666)
 * complete la classe java.lang.Math de la bibliotheque java
 */
public interface Math2 {
    /**
     *  double (approximativement 2π)
     */
    public static double PI2 = 2*Math.PI; 
    
    /**
     * retourne x élevé au carré,
     * @param x le double a mettre au carre
     * @return x au carre
     */
    public static double sq(double x){
        return x*x;
    }
    
    /**
     * retourne le reste de la division entière par défaut de x par y,
     * @param x double 
     * @param y double 
     * @return le reste de la division entière par défaut de x par y,
     */
    public static double floorMod(double x, double y){
        return x - y*Math.floor(x/y);
    }
    
    /**
     * retourne sin(x/2)²
     * @param x 
     * @return sin(x/2)²
     */
    public static double haversin(double x){
        return sq(Math.sin(x/2));
    }
    
    /**
     * retourne la distance angulaire en radians entre deux angles
     * @param a1 angle 1
     * @param a2 angle 2
     * @return la distance angulaire en radians  entre les deux angles
     */
    public static double angularDistance(double a1, double a2){
        return  floorMod(a2-a1+Math.PI, PI2) - Math.PI;
    }
    
    /**
     * retourne la valeur de f(x) obtenue par interpolation linéaire, 
     * sachant que f(0) vaut y0 et f(1) vaut y1 
     * x0 et x1 doit etre separer de 1 unite
     * @param y0 valeur de f(0)
     * @param y1 valeur de f(1)
     * @param x peut être quelconque
     * @return la valeur de f(x) obtenue par interpolation linéaire, 
     * sachant que f(0) vaut y0 et f(1) vaut y1
     */
    public static double lerp(double y0, double y1, double x){
        return (y1-y0)*x+y0;
    }
    
    /**
     * retourne la valeur de f(x,y) obtenue par interpolation bilinéaire, 
     * sachant que f(0,0) vaut z00, f(1,0) vaut z10, f(0,1) 
     * vaut z01 et f(1,1) vaut z11 ; 
     * @param z00 valeur de f(0,0)
     * @param z10 valeur de f(1,0)
     * @param z01 valeur de f(0,1)
     * @param z11 valeur de f(1,1)
     * @param x quelconque
     * @param y quelconque
     * @return retourne la valeur de f(x,y) obtenue par interpolation bilinéaire, 
     */
    public static double bilerp(double z00, double z10, double z01, double z11, double x, double y){
       double lerp0 = lerp(z00, z10, x);
       double lerp1 = lerp(z01, z11, x);
       return lerp (lerp0, lerp1, y);
        
    }
    
    /**
     * la borne inferieur du premier intervalle contenant 0, ou POSITIVE_INFINITY si il n'y en pas.
     * @param f DoubleUnaryOperator fonction ou on veux trouver le premier intervalle avec un 0
     * @param minX borne inf de l'intervalle contenant 0
     * @param maxX borne suf de l'intervalle contenant 0
     * @param dX taille de lintervalle
     * @return la borne inferieur du premier intervalle contenant 0, ou POSITIVE_INFINITY si il n'y en pas.
     */
    public static double firstIntervalContainingRoot(DoubleUnaryOperator f, double minX, double maxX, double dX){
       double m = minX;
       double f1 = 0;
       double f2 = 0;
      
       Preconditions.checkArgument((minX <= maxX && 0 < dX), "les bornes ne sont pas dans un ordre logique ou l'intervalle de recherche est négatif");  
       
       while (m <=maxX-dX){ 
          f1 = f.applyAsDouble(m); 
          f2 = f.applyAsDouble(m+dX);
          
          if (f1*f2<=0) {
              return m;
          }
          m+=dX;
      }  
       return Double.POSITIVE_INFINITY;
          
    }   
    
    /**
     * retourne une approximation de l'antécédant de l'élément 0 à une précision de epsilon près dans un intervalle.  
     * @param f fonction DoubleUnaryOperator 
     * @param x1 borne inf de l'intervalle avec possiblement un 0
     * @param x2 borne sup de l'intervalle avec possiblement un 0
     * @param epsilon (la precision de recherche)
     * @return retourne une approximation de l'antécédant de l'élément 0 à une précision de epsilon près dans un intervalle. 
     * @throws IllegalArgumentException s'il n'y a pas de 0 dans l'intervalle
     */
    public static double improveRoot(DoubleUnaryOperator f, double x1, double x2, double epsilon){
        Preconditions.checkArgument(!(f.applyAsDouble(x1)*f.applyAsDouble(x2) > 0 ));
        double inf = x1;
        double sup = x2;
        double fm=0;
        double fxsup=0;
        double fxinf=0;
        while (sup-inf>epsilon){
            double m = (inf+sup)/2;
            fm = f.applyAsDouble(m);
            fxinf = f.applyAsDouble(inf);
            fxsup = f.applyAsDouble(sup);
            if (fm==0){
                return m;
            } else if (fm*fxinf<=0){
                sup=m;
            } else if (fm*fxsup<=0){
                inf=m;
            }
        }
        return inf;
    }
}
