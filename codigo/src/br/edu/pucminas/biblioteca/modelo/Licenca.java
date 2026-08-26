package br.edu.pucminas.biblioteca.modelo;
//Nota de Transparência sobre Uso de IA
// pedi auxilio do claude para sugestão de exceções 
public class Licenca {
    private final int limiteAcessosSimultaneos = 60;
    private final int minimoAcessosPorSemestre = 3;
    private int acessosAtivos;

    public Licenca (){
        this.acessosAtivos = 0;
    }
    
    public boolean temVagaDisponivel(Ebook ebook){
        return (acessosAtivos < limiteAcessosSimultaneos);
    } 
    
    public boolean acessosAtivosMenorQueMinimoPermitido(){
        return (acessosAtivos < minimoAcessosPorSemestre);
    }

    public void incrementarAcessosAtivos(){
        if (acessosAtivos >= limiteAcessosSimultaneos)
            throw new IllegalStateException("Limite de acessos simultâneos atingido");
        this.acessosAtivos++;
    }

    public void decrementarAcessosAtivos(){
        if (acessosAtivos <= 0)
            throw new IllegalStateException("Não é possível decrementar: não há acessos ativos");
        this.acessosAtivos--;
    }

    public int getAcessosAtivos() {
        return acessosAtivos;
    }
}
