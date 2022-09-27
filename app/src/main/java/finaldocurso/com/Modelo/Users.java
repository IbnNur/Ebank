package finaldocurso.com.Modelo;

public class Users
{
    String nome, numero_conta, senha, bi, data_nasc, local_nasc, saldo, estado_civil;

    public Users()
    {
    }

    public Users(String nome, String numero_conta, String senha, String bi, String data_nasc,
                 String local_nasc, String endereco, String estado_civil) {
        this.nome = nome;
        this.numero_conta = numero_conta;
        this.senha = senha;
        this.bi = bi;
        this.data_nasc = data_nasc;
        this.local_nasc = local_nasc;
        this.saldo = endereco;
        this.estado_civil = estado_civil;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNumero_conta() {
        return numero_conta;
    }

    public void setNumero_conta(String numero_conta) {
        this.numero_conta = numero_conta;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getBi() {
        return bi;
    }

    public void setBi(String bi) {
        this.bi = bi;
    }

    public String getData_nasc() {
        return data_nasc;
    }

    public void setData_nasc(String data_nasc) {
        this.data_nasc = data_nasc;
    }

    public String getLocal_nasc() {
        return local_nasc;
    }

    public void setLocal_nasc(String local_nasc) {
        this.local_nasc = local_nasc;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String endereco) {
        this.saldo = endereco;
    }

    public String getEstado_civil() {
        return estado_civil;
    }

    public void setEstado_civil(String estado_civil) {
        this.estado_civil = estado_civil;
    }
}
