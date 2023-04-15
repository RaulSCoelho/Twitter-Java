import java.util.ArrayList;
import java.util.Scanner;

public class Usuario {
  private Scanner input = new Scanner(System.in);

  private String nome, login, email, senha;
  private boolean logged;
  private ArrayList<String> tweets = new ArrayList<String>();

  public Usuario(String nome, String login, String email, String senha) {
    this.nome = validateString(nome, 2, 30, "Nome");
    this.login = validateString(login, 2, 20, "Login");
    this.email = validateString(email, 6, 30, "Email");
    this.senha = validateString(senha, 6, 15, "Senha");
    this.logged = true;
  }

  // #region Métodos restritos por usuário
  @Override
  public String toString() {
    return String.format("\nNome: %s\nLogin: %s\nEmail: %s", nome, login, email);
  }

  public String getNome() {
    return nome;
  }

  public String getLogin() {
    return login;
  }

  // #region Auth Section
  public boolean signIn(String user, String password) {
    if (login.equals(user) && senha.equals(password)) {
      logged = true;
      return true;
    } else {
      System.out.println("Login ou senha inválidos");
      return false;
    }
  }

  public void logOut() {
    logged = false;
  }

  public boolean isLogged() {
    return logged;
  }

  public void changePassword(String oldPassword, String newPassword) {
    if (!isPasswordValid(oldPassword))
      return;

    senha = newPassword;
  }
  // #endregion

  // #region Tweets Section
  public ArrayList<String> getTweets() {
    return tweets;
  }

  public void tweet(String tweet) {
    if (!isTweetValid(tweet))
      return;

    tweets.add(tweet);
  }
  // #endregion
  // #endregion

  // #region Métodos utilitários
  private String validateString(String str, int min, int max, String field, boolean askAgain) {
    if (str.length() < min || str.length() > max) {
      System.out.println(String.format("%s deve ter de %d a %d caracteres.\nDigite novamente: ", field, min, max));

      if (!askAgain)
        return null;
      return validateString(input.nextLine(), min, max, field);
    }

    return str;
  }

  private String validateString(String str, int min, int max, String field) {
    return validateString(str, min, max, field, true);
  }

  private boolean isPasswordValid(String password) {
    boolean valid = senha.equals(password);
    if (!valid)
      System.out.println("Senha incorreta!");
    return valid;
  }

  private boolean isTweetValid(String tweet) {
    if (validateString(tweet, 1, 140, "Tweet").isBlank())
      return false;

    for (String t : tweets) {
      if (t.equals(tweet)) {
        System.out.println("Você ja escreveu esta mensagem. Escreva outra.");
        return false;
      }
    }

    return true;
  }
  // #endregion
}