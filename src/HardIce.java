public class HardIce extends ElementoBasico {

    

    public HardIce(String id, int linInicial, int colInicial, Tabuleiro tabuleiro) {
        super(id, "gelinref.jpg",  linInicial, colInicial, tabuleiro);
        
    }

    @Override
    public void acao(ElementoBasico outro) {
    
    
      getTabuleiro().insereElemento(new Ice("ice", getLin(), getCol(), getTabuleiro()));
    
    

    }

    
    
}
