import java.awt.*;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

public class Tabuleiro extends JPanel {
    private static final int MAXLIN = 15;
    private static final int MAXCOL = 19;
    private boolean isLevelComplete = false;
    private ElementoBasico[][] celulas;

    private Personagem principal;

    public Tabuleiro() {
        super();
        // Cria o conjunto de células vazia e as insere no layout
        celulas = new ElementoBasico[MAXLIN][MAXCOL];
        this.setLayout(new GridLayout(MAXLIN, MAXCOL));
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {

                celulas[i][j] = new Fundo("Fundo[" + i + "][" + j + "]", i, j, this);
                this.add(celulas[i][j]);
            }
        }
    }

    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = App.class.getResource("imagens/" + path);

        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            System.exit(0);
            return null;
        }
    }

    public static int getMaxlin() {
        return MAXLIN;
    }

    public static int getMaxcol() {
        return MAXCOL;
    }

    public boolean posicaoValida(int lin, int col) {
        if ((lin < 0) || (col < 0) ||
                (lin >= MAXLIN) || (col >= MAXCOL)) {
            return false;
        } else {
            return true;
        }
    }

    // Retorna referencia em determinada posição
    public ElementoBasico getElementoNaPosicao(int lin, int col) {
        if (!posicaoValida(lin, col)) {
            return null;
        }
        return celulas[lin][col];
    }

    public ElementoBasico insereElemento(ElementoBasico elemento) {
        int lin = elemento.getLin();
        int col = elemento.getCol();
        if (!posicaoValida(lin, col)) {
            throw new IllegalArgumentException("Posicao invalida:" + lin + " ," + col);
        }
        ElementoBasico elementoAnterior = celulas[lin][col];
        celulas[lin][col] = elemento;
        return elementoAnterior;
    }

    public void atualizaVisualizacao() {
        // Atualiza o conteúdo do JPanel (ver algo melhor)
        this.removeAll(); // erase everything from your JPanel
        this.revalidate();
        this.repaint();// I always do these steps after I modify my JPanel
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {
                this.add(celulas[i][j]);
            }
        }
    }

    public void loadLevel(int nivel) {
        Path path1 = Paths.get(String.format("nivel%d.txt", nivel));
        try (BufferedReader reader = Files.newBufferedReader(path1)) {
            String line = null;
            int lin = 0;
            while ((line = reader.readLine()) != null) {

                for (int col = 0; col < MAXCOL; col++) {
                    if (line.charAt(col) != ' ') {
                        ElementoBasico elem = this.getElem(line.charAt(col), lin, col);
                        this.insereElemento(elem);
                    }
                }
                lin++;

            }
        } catch (IOException x) {
            System.err.format("Erro de E/S: %s%n", x);
        }
    }

    public void reset() {
        this.isLevelComplete = false;
        this.removeAll();
        this.revalidate();
        this.repaint();
        for (int i = 0; i < MAXLIN; i++) {
            for (int j = 0; j < MAXCOL; j++) {
                celulas[i][j] = new Fundo("Fundo[" + i + "][" + j + "]", i, j, this);
                this.add(celulas[i][j]);
            }
        }
    }

    public void completeLevel() {
        this.isLevelComplete = true;
    }

    public boolean isLevelComplete() {
        return isLevelComplete;
    }

    public ElementoBasico getElem(char elem, int lin, int col) {
        Random r = new Random();
        switch (elem) {
            case ' ':
                return new Fundo("Fundo", lin, col, this);
            case 'i':
                return new Ice("Gelo", lin, col, this);
            case '-':
                return new Wall("Parede", lin, col, this);
            case  '!': 
                return new HardIce("GeloDuro", lin, col, this);
            case '+':
                return new FinalTile("Final", "chegada.jpg", lin, col, this);
            case 'f': 
                return new  Coin("Moeda", lin, col, this);
            case '*': {
                ElementoBasico anterior = new Fundo("Fundo", lin, col, this);
                principal = new Personagem("Puffle", "puffle_player.png", lin, col, this);
                principal.setAnterior(anterior);
                return principal;
            }
          
            default:
                throw new IllegalArgumentException("Personagem invalido: " + elem);
        }

    }

    public Personagem getPrincipal() {
        return principal;
    }
}
