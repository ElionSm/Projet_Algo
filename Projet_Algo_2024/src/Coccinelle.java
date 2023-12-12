
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.Double;
import java.util.Arrays;
import java.util.List;
import java.util.Random; 
public class Coccinelle {
    // Question 1
    static int glouton(int[][] G, int d){
        int nb_pucerons_manges = G[0][d];
        int C = G[0].length - 1;
        int L = G.length - 1;
        int x = d;
        int y = 0;
        // la coccinelle commence a G[0][d]
       
        while (y != L) {
            
            if (x - 1 < 0) { // la case en bas a gauche n'existe pas, on compare que bas et bas_droite
                int bas = G[y + 1][x];
                int bas_droite = G[y + 1][x + 1];

                if (bas_droite > bas) {
                    x = x + 1;
                }
                y = y + 1;
            } else if (x + 1 > C) {
                int bas = G[y + 1][x];
                int bas_gauche = G[y + 1][x - 1];

                if (bas_gauche > bas) {
                    x = x - 1;
                }

                y = y + 1;

            } else {
                int bas = G[y + 1][x];
                int bas_droite = G[y + 1][x + 1];
                int bas_gauche = G[y + 1][x - 1];

                if (bas_droite > bas && bas_droite > bas_gauche) {
                    x = x + 1;

                } else {
                    x = x - 1;

                }
                y = y + 1;
            }
            nb_pucerons_manges += G[y][x];

        }
    

        return nb_pucerons_manges;
    }
    //Question 2
   static int[] glouton(int[][] G) {
        int n = G[0].length;
        int[] nb_pucerons_manges = new int[n];
        for (int i = 0; i < n ; i++) {
            nb_pucerons_manges[i] = glouton(G, i);
        }
        return nb_pucerons_manges;
    }
    //Question 4
    static int[][][] calculerMA(int[][] G, int d) {
        int C = G[0].length;
        int L = G.length;
        int[][] M = new int[L][C];
        int[][] A = new int[L][C];
    
        for (int c = 0; c < C; c++) { //Initialisation de la première ligne de M et A
            M[0][d] = G[0][d]; //la coccinelle commence à (0,d)
            if (c != d) { // Si la coccinelle ne commence pas à (0,d), on met -1 dans M et A
                M[0][c] = -1; // -1 signifie que la coccinelle ne peut pas aller à cette case
                A[0][c] = -1; 
            } else {
                A[0][c] = c; // Si la coccinelle commence à (0,d), on met d dans A
            }
        }
    
        for (int l = 1; l < L; l++) { 
            for (int c = 0; c < C; c++) {
                if (c - 1 < 0) { //si la case en bas a gauche n'existe pas
                    int maxVal = max(M[l - 1][c], M[l - 1][c + 1]); // On calcule le max entre les deux cases en bas
                    if (maxVal != -1) { // Si le max est différent de -1, on peut aller à cette case
                        M[l][c] = maxVal + G[l][c]; // On ajoute la valeur de la case à la valeur max
                        A[l][c] = (maxVal == M[l - 1][c]) ? c : (c + 1); // On met la colonne de la case max dans A
                    } else { 
                        M[l][c] = -1; // Si le max est -1, on ne peut pas aller à cette case
                        A[l][c] = -1;
                    }
                } else if (c + 1 >= C) { //si la case en bas a droite n'existe pas
                    int maxVal = max(M[l - 1][c - 1], M[l - 1][c]); // On calcule le max entre les deux cases en bas
                    if (maxVal != -1) { // Si le max est différent de -1, on peut aller à cette case
                        M[l][c] = maxVal + G[l][c]; // On ajoute la valeur de la case à la valeur max
                        A[l][c] = (maxVal == M[l - 1][c]) ? c : (c - 1); // On met la colonne de la case max dans A
                    } else {
                        M[l][c] = -1;
                        A[l][c] = -1;
                    }
                } else {
                    int maxVal = max(M[l - 1][c], M[l - 1][c - 1], M[l - 1][c + 1]);
                    if (maxVal != -1) {
                        M[l][c] = maxVal + G[l][c];
                        if (maxVal == M[l - 1][c]) {
                            A[l][c] = c;
                        } else if (maxVal == M[l - 1][c - 1]) {
                            A[l][c] = c - 1;
                        } else {
                            A[l][c] = c + 1;
                        }
                    } else {
                        M[l][c] = -1;
                        A[l][c] = -1;
                    }
                }
            }
        }
        return new int[][][] { M, A };
    }
    //question 6
    static int optimal(int[][]G,int d){
        int[][][] MA = calculerMA(G,d);
		int[][] M = MA[0];
        int  L = M.length; // nombre de lignes de M
        int cStar = argMax(M[L-1]) ; // colonne d’arriv´ee du chemin max. d’origine (0,d)
        return M[L-1][cStar];
    }
    //Question 7
    static int[] optimal(int[][] G) {
        int n = G[0].length;
        int[] nb_pucerons_manges = new int[n];
        for (int i = 0; i < n ; i++) {
            nb_pucerons_manges[i] = optimal(G, i);
        }
        return nb_pucerons_manges;
    }
    //Question 8
    static float[] gainRelatif(int[] Nmax, int[] Ng){
        int n = Ng.length;
        float[] Gains = new float[n];
        for (int i = 0; i < n ; i++) {
            Gains[i] = (float)(Math.round(((float)(Nmax[i]-Ng[i])/Ng[i])*1000.0)/1000.0);
        }
        return Gains;
    }
    //Question 5
    static void acnpm(int[][] M, int[][] A){
        int  L = M.length; // nombre de lignes de M
        int cStar = argMax(M[L-1]) ; // colonne d’arriv´ee du chemin max. d’origine (0,d)
        acnpm(A, L-1, cStar); // affichage du chemin maximum de (0,d) `a (L-1, cStar)
        System.out.print(" Valeur : ");
        }
    static int argMax(int[]M){int  L = M.length;
        int max = M[0];
        int index = 0;
        for(int i = 0; i < L;i++){
            if(M[i] > max ){ 
                max = M[i];
                index = i;
            }
        }
        return index;
    }
    static void acnpm(int[][] A, int l, int c){
        if (l == 0) System.out.print("("+l+","+c+")");
        else {
            int cStar = A[l][c] ; // colonne d’arriv´ee du chemin max. de (0,d) `a (l,c)
            acnpm(A, l-1, cStar); // affichage du chemin maximum de (0,d) `a (l-1, cStar)
            System.out.printf("(%d,%d)",l,c);
            }
        }

    static void afficherTableauG(int[][] T){ int n = T.length;
        for (int i = n-1; i > -1; i--) 
            System.out.printf("G[%d] : %s\n", i, Arrays.toString(T[i]));
        }
            
    static void afficherTableauM(int[][] T){ int n = T.length;
        for (int k = n-1; k > -1; k--) 
            System.out.printf("M[%d] : %s\n", k, Arrays.toString(T[k]));
        }
        //fin question 5
    static int max(int x, int y){ if (x >= y) return x; return y;}
    static int min(int x, int y){ if (x <= y) return x; return y;}
    static int max(int x, int y, int z){ if (x >= max(y,z)) return x; 
        if (y >= z) return y; 
        return z;
    }
    //Question 9
    static int[] permutationAleatoire(int[] T){ int n = T.length;
    // Calcule dans T une permutation al´eatoire de T et retourne T
    Random rand = new Random(); // biblioth`eque java.util.Random
    for (int i = n; i > 0; i--){
        int r = rand.nextInt(i); // r est au hasard dans [0:i]
        permuter(T,r,i-1);
    }
    return T;
    }
    static void permuter(int[] T, int i, int j){
    int ti = T[i];
    T[i] = T[j];
    T[j] = ti;
    }
    public static int[][] genererGrilleAleatoire() {
        Random random = new Random();
        int c = random.nextInt(12)+5;
        int l = random.nextInt(12)+5;
        int[][] G = new int[l][c];
        // Remplissage de la grille G avec des valeurs aléatoires
        for(int i = 0;i < c;i++){
            for(int j = 0; j < l;j++){
                G[j][i] = random.nextInt(l*c);
            }
        }
        // Permutation aléatoire des éléments dans la grille
        int[] elements = new int[l * c];
        int k = 0;
         // Extraction des éléments de la grille dans un tableau 1D
         for (int i = 0; i < c; i++) {
            for (int j = 0; j < l; j++) {
                elements[k++] = G[j][i];
            }
        }
        // Permutation aléatoire des éléments
        permutationAleatoire(elements);
        // Remplissage de la grille avec les éléments permutés
        k = 0;
        for (int i = 0; i < c; i++) {
            for (int j = 0; j < l; j++) {
                G[j][i] = elements[k++];
            }
        }
        return G;
      }


    public static double min(double[][] pTab){
        double min = pTab[0][0]; // On initialise la valeur min à la première valeur du tableau
        for(int i = 0; i < pTab.length; i++){ 
            for(int j = 0; j < pTab[i].length; j++){
                if(pTab[i][j] < min){
                    min = pTab[i][j]; // Si la valeur est inférieure à la valeur min, on la remplace
                }
            }
        }
        return min;
    }

    public static double max(double[][] pTab){
        double max = pTab[0][0];
        for(int i = 0; i < pTab.length; i++){
            for(int j = 0; j < pTab[i].length; j++){
                if(pTab[i][j] > max){
                    max = pTab[i][j]; // Si la valeur est supérieure à la valeur max, on la remplace
                }
            }
        }
        return max;
    }

    public static double mean(double[][] pTab){
        double mean = 0;
        for(int i = 0; i < pTab.length; i++){
            for(int j = 0; j < pTab[i].length; j++){
                mean += pTab[i][j]; // On additionne toutes les valeurs du tableau
            }
        }
        mean = mean / (pTab.length * pTab[0].length); // On divise la somme par le nombre de valeurs
        return mean;
    }

    public static double med(double[][] pTab) {
        int n = pTab.length * pTab[0].length; // On calcule le nombre de valeurs du tableau
        List<Double> values = new ArrayList<>(); // On crée une liste pour stocker les valeurs du tableau

        for (int i = 0; i < pTab.length; i++) {
            for (int j = 0; j < pTab[i].length; j++) {
                values.add(pTab[i][j]); // On ajoute les valeurs du tableau à la liste
            }
        }

        double[] sortedValues = values.stream().mapToDouble(Double::doubleValue).sorted().toArray(); // On trie les valeurs de la liste dans un tableau

        if (n % 2 == 0) { 
            return (sortedValues[n / 2] + sortedValues[(n / 2) - 1]) / 2; // Si le nombre de valeurs est pair, on calcule la moyenne des deux valeurs du milieu
        } else {
            return sortedValues[(n - 1) / 2]; // Si le nombre de valeurs est impair, on retourne la valeur du milieu
        }
    }
    public static void writeCSV(double[][] data, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Création du fichier CSV s'il n'existe pas
            writer.write(""); // Écriture d'une chaîne vide pour créer le fichier
            for (int i = 0; i < data.length; i++) { // Écriture des données
                for (int j = 0; j < data[i].length; j++) { // Écriture d'une valeur
                    writer.write(String.valueOf(data[i][j])); // Conversion de la valeur en chaîne de caractères
                    if (j < data[i].length - 1) { 
                        writer.write(",");// Ajout d'une virgule si on n'est pas à la dernière valeur
                    }
                }
                writer.newLine(); // Saut de ligne
            }
            System.out.println("\nLes gains relatifs sont dans le fichier " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //Fin question 9


    
    public static void main(String[] args) throws Exception {
        int[][] G = {{2,2,3,4,2},
                     {6,5,1,2,8},
                     {1,1,10,1,1}};
        System.out.println("grille G :");
        afficherTableauG(G);
        System.out.print("Valeurs des chemins gloutons depuis les cases (0,d) : ");
        System.out.print("Ng = "+Arrays.toString(glouton(G))+"\n");
        System.out.println("Programmation dynamique, case de depart (0, 0)");
        System.out.println("M : ");
        int[][][] MA1 = calculerMA(G,0);
		int[][] M1 = MA1[0], A1 = MA1[1];// Séparation des tableaux M et A
        afficherTableauM(M1);
        System.out.print("un chemin maximum : ");
        acnpm(M1, A1); //Affichage des chemins
        //Exercice 6
        System.out.print(optimal(G, 0));
        System.out.println("\nProgrammation dynamique, case de depart (0, 1)");
        System.out.println("M : ");
        int[][][] MA2 = calculerMA(G,1);
		int[][] M2 = MA2[0], A2 = MA2[1];
        afficherTableauM(M2);
        System.out.print("un chemin maximum : ");
        acnpm(M2, A2);
        System.out.print(optimal(G, 1)+"\n");
        System.out.println("\nChemins max. depuis toutes les cases de depart (0,d)");
        for(int i = 0; i < G[0].length;i++){
            int[][][] MA = calculerMA(G,i);
		    int[][] M = MA[0], A = MA[1]; 
            System.out.print("\nun chemin maximum : ");
            acnpm(M, A); //Affichage du chemin
            System.out.print(optimal(G, i));
        }
        //Exercice 7
        int[] Ng = glouton(G);
        int[] Nmax = optimal(G);
        System.out.print("\n\nNg = "+Arrays.toString(glouton(G))+"\n");
        System.out.print("Nmax = "+Arrays.toString(optimal(G))+"\n");
        
        System.out.print("Gains relatifs = "+Arrays.toString(gainRelatif(Nmax,Ng))+"\n");
        //Exercice 9
        int nruns = 10000;
        float[] resultGains = new float[0];
        double[][] gain = new double[nruns+1][]; 
        int GainLength = 0;
        System.out.println("\nVALIDATION STATISTIQUE");
        System.out.println("nruns : 10000");
         for(int i = 0;i <= nruns;i++){
        int[][] vG = genererGrilleAleatoire();
        int C = vG[0].length;
        int L = vG.length;
         // Calculer les gains relatifs et les stocker dans le tableau resultGains
            int[] Nmax2 = optimal(vG);
            int[] Ng2 = glouton(vG);
            resultGains = gainRelatif(Nmax2, Ng2);
            gain[i] = new double[resultGains.length];// On crée un tableau de la taille de resultGains
            for(int j = 0; j < resultGains.length; j++){
                gain[i][j] = resultGains[j]; // On copie les valeurs de resultGains dans gain
            }
            GainLength += resultGains.length; // On ajoute la taille de resultGains à GainLength
        
        // Afficher les informations toutes les 100 boucles
        if (i % 1000 == 0) {
            System.out.printf("run %d/10000, (L,C) = (%d,%d)%n", i, L, C);
        }
    }
    System.out.printf("GAINS.length=%d , min=%f, max=%f, mean=%f, med=%f",GainLength,min(gain),max(gain),mean(gain),med(gain));
    String File = "gainsRelatifs_nruns=10000_Linf=5_Lsup=16_Cinf=5_Csup=16.csv";
    writeCSV(gain, File); // Écriture des gains relatifs dans un fichier CSV


        //System.out.println(Arrays.deepToString(calculerMA(G1, 0)));
    }
}




