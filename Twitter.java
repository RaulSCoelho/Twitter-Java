import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Twitter {
  static Scanner input = new Scanner(System.in);
  static ArrayList<Usuario> users = new ArrayList<Usuario>(); // Lista de usuários
  private static ArrayList<String> feed = new ArrayList<String>(); // Lista de todos os tweets
  private static ArrayList<String> tweetOwners = new ArrayList<String>(); // Lista dos donos dos tweets

  public static void main(String[] args) {
    boolean endProgram = false;

    do {
      int action = chooseAction();

      System.out.println();

      switch (action) {
        case 1:
          createUser();
          break;
        case 2:
          printUsers();
          break;
        case 3:
          signIn();
          break;
        case 4:
          logOut();
          break;
        case 5:
          tweet();
          break;
        case 6:
          printLastNTweets();
          break;
        case 7:
          removeTweet();
          break;
        case 8:
          changePassword();
          break;
        case 9:
          // #region REMOVER USUÁRIO
          break;
        // #endregion
        case 10:
          // #region IMPRIMIE ESTATÍSTICAS
          break;
        // #endregion
        default:
          // FINALIZAR-PROGRAMA
          endProgram = true;
      }

      System.out.println();
    } while (!endProgram);
  }

  public static int chooseAction() {
    System.out.println("Escolha uma ação: ");
    System.out.println("1 - Cadastrar usuário");
    System.out.println("2 - Listar usuários");
    System.out.println("3 - Logar usuário ");
    System.out.println("4 - Deslogar usuário");
    System.out.println("5 - Tweetar");
    System.out.println("6 - Mostrar últimos tweets do feed");
    System.out.println("7 - Remover tweet de um usuário");
    System.out.println("8 - Alterar senha de um usuário");
    System.out.println("9 - Remover um usuário");
    System.out.println("10- Imprimir estatísticas");
    System.out.println("0 - Finalizar programa");

    return nextInt();
  }

  // #region Actions
  public static void createUser() {
    System.out.println("Para cadastrar um usuário será necessários algumas informações");
    String nome = nextLine("Nome: ");
    String login = nextLine("Login: ");
    String email = nextLine("Email: ");
    String senha = nextLine("Senha: ");

    if (findUser(login, false) != null) {
      System.out.println("Usuario com esse login ja existe!");
      return;
    }

    users.add(new Usuario(nome, login, email, senha));
    System.out.println("Usuário cadastrado :)");
  }

  public static void printUsers() {
    System.out.println("Usuários: ");
    for (Usuario user : users) {
      System.out.println(user.getLogin() + ": " + (user.isLogged() ? "Logado" : "Não logado"));
    }
  }

  public static void signIn() {
    Usuario user = findUser(true);

    if (user != null && !user.isLogged())
      user.signIn(user.getLogin(), nextLine("Senha: "));
    else
      System.out.println("Usuário já logado!!");
  }

  public static void logOut() {
    Usuario user = findUser(true);

    if (user != null && user.isLogged()) {
      user.logOut();
    } else {
      System.out.println("Usuário já deslogado!!");
    }
  }

  public static void tweet() {
    Usuario user = findUser(true);

    if (user != null && user.isLogged()) {
      String tweet = nextLine("Tweet: ");

      user.tweet(tweet);
      tweetOwners.add(user.getLogin());
      feed.add(tweet);
    } else {
      System.out.println("Primeiro realize o login!");
    }
  }

  public static void printLastNTweets() {
    ArrayList<String> lastNTweets = getLastNTweets(nextInt("Quantos tweets deseja ver? "));
    for (String tweet : lastNTweets) {
      System.out.println(tweet);
    }
  }

  public static void removeTweet() {
    Usuario user = findUser(true);

    if (user == null)
      return;

    if (!user.isLogged()) {
      System.out.println("Primeiro realize o login!");
      return;
    }

    ArrayList<String> userTweets = user.getTweets();

    System.out.println("Selecione um tweet: ");
    for (int i = 0; i < userTweets.size(); i++) {
      System.out.println((i + 1) + userTweets.get(i));
    }

    removeTweet(userTweets.get(nextInt() - 1), user.getLogin());
  }

  public static void changePassword() {
    Usuario user = findUser(true);

    if (user != null)
      user.changePassword(nextLine("Senha antiga: "), nextLine("Senha nova: "));
  }
  // #endregion

  // #region "Api"
  public static Usuario findUser(boolean showMessage) {
    String login = nextLine("Login do usuário: ");
    return findUser(login, showMessage);
  }

  public static Usuario findUser(String login, boolean showMessage) {
    for (Usuario user : users) {
      if (user.getLogin().equals(login)) {
        return user;
      }
    }

    if (showMessage)
      System.out.println("Usuário não encontrado!");

    return null;
  }

  public static ArrayList<String> getLastNTweets(int nTweets) {
    if (feed.size() < nTweets)
      return feed;

    int startIndex = feed.size() - nTweets;
    return new ArrayList<String>(feed.subList(startIndex, feed.size()));
  }

  public static void removeTweet(String tweet, String user) {
    Iterator<String> ownerIterator = tweetOwners.iterator();
    Iterator<String> feedIterator = feed.iterator();

    boolean found = false;
    while (ownerIterator.hasNext() && feedIterator.hasNext()) {
      String owner = ownerIterator.next();
      String feedItem = feedIterator.next();

      if (owner.equals(user) && feedItem.equals(tweet)) {
        ownerIterator.remove();
        feedIterator.remove();
        break;
      }
    }

    if (!found)
      System.out.println("Tweet não encontrado!");
  }
  // #endregion

  // #region Utils
  public static String nextLine(String message) {
    System.out.println(message);
    return nextLine();
  }

  public static String nextLine() {
    return input.nextLine();
  }

  public static int nextInt(String message) {
    System.out.println(message);
    return nextInt();
  }

  public static int nextInt() {
    int number = input.nextInt();
    input.nextLine();
    return number;
  }

  public static double nextDouble(String message) {
    System.out.println(message);
    return nextDouble();
  }

  public static double nextDouble() {
    double number = input.nextDouble();
    input.nextLine();
    return number;
  }
  // #endregion
}
