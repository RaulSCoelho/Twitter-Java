import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Twitter {
  static Scanner input = new Scanner(System.in);
  static ArrayList<Usuario> users = new ArrayList<Usuario>(); // Lista de usuários
  static ArrayList<String> feed = new ArrayList<String>(); // Lista de todos os tweets
  static ArrayList<String> tweetOwners = new ArrayList<String>(); // Lista dos donos dos tweets

  public static void main(String[] args) {
    boolean endProgram = false;

    About();

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
          removeUser();
          break;
        case 10:
          printInfos();
          break;
        default:
          endProgram = true;
      }

      System.out.println();
    } while (!endProgram);
  }

  public static int chooseAction() {
    // Função que retorna a opção escolhida pelo usuário
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
    System.out.println("10 - Imprimir estatísticas");
    System.out.println("0 - Finalizar programa");

    return nextInt();
  }

  // #region Actions
  public static void createUser() {
    // Função que recebe as informações do usuário e cria um novo usuário
    System.out.println("Para cadastrar um usuário será necessários algumas informações");
    String nome = nextLine("Nome: ");
    String login = nextLine("Login: ");
    String email = nextLine("Email: ");
    String senha = nextLine("Senha: ");

    // Verifica se o usuário digitado já existe
    if (findUser(login, false) != null) {
      System.out.println("Usuario com esse login ja existe!");
      return;
    }

    // Guardando esse usuário no arraylist users
    users.add(new Usuario(nome, login, email, senha));
    System.out.println("Usuário cadastrado e logado :)");
  }

  public static void printUsers() {
    System.out.println("Usuários: ");
    for (Usuario user : users) {
      System.out.println(user.getLogin() + ": " + (user.isLogged() ? "Logado" : "Não logado"));
    }
  }

  public static void signIn() {
    Usuario user = findUser(true);

    if (user == null)
      return;

    if (!user.isLogged())
      user.signIn(user.getLogin(), nextLine("Senha: "));
    else
      System.out.println("Usuário já logado!!");
  }

  public static void logOut() {
    Usuario user = findUser(true);

    if (user == null)
      return;

    if (user.isLogged())
      user.logOut();
    else
      System.out.println("Usuário já deslogado!!");
  }

  public static void tweet() {
    Usuario user = findUser(true);

    if (user == null)
      return;

    if (user.isLogged()) {
      String tweet = nextLine("Tweet: ");

      user.tweet(tweet);
      tweetOwners.add(user.getLogin());
      feed.add(tweet);
    } else {
      System.out.println("Primeiro realize o login!");
    }
  }

  public static void printLastNTweets() {
    int nTweets = nextInt("Quantos tweets deseja ver? ");
    ArrayList<String> lastNTweets = getLastNTweets(nTweets);
    ArrayList<String> lastNTweetOwners = getLastNTweetOwners(nTweets);

    for (int i = 0; i < lastNTweets.size(); i++) {
      String owner = lastNTweetOwners.get(i);
      String tweet = lastNTweets.get(i);
      System.out.println(String.format("%s - %s", owner, tweet));
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
      System.out.println(String.format("%d - %s", i + 1, userTweets.get(i)));
    }

    removeTweet(userTweets.get(nextInt() - 1), user);
  }

  public static void changePassword() {
    Usuario user = findUser(true);

    if (user != null)
      user.changePassword(nextLine("Senha antiga: "), nextLine("Senha nova: "));
  }

  public static void removeUser() {
    Iterator<Usuario> userIterator = users.iterator();
    Usuario user = findUser(true);

    if (!user.signIn(user.getLogin(), nextLine("Digite a senha do usuario: ")))
      return;

    while (userIterator.hasNext()) {
      if (userIterator.next().equals(user)) {
        removeUserTweets(user);
        userIterator.remove();
        break;
      }
    }
  }

  public static void printInfos() {
    int loggedUsers = 0;

    for (Usuario user : users) {
      if (user.isLogged())
        loggedUsers++;
    }

    System.out.println("Estatísticas: ");
    System.out.println("Número de usuários: " + users.size());
    System.out.println("Número de usuários logados: " + loggedUsers);
    System.out.println("Número de tweets: " + feed.size());
    System.out.println("Número de tweets por usuário: ");

    for (Usuario user : users) {
      System.out.println(user.printTweets());
    }

    int maior = 0;
    Usuario tweetedTheMost = users.get(0);

    for (Usuario user : users) {
      int tweetsSize = user.getTweets().size();

      if (tweetsSize > maior) {
        maior = tweetsSize;
        tweetedTheMost = user;
      }
    }

    System.out.println(
        String.format("Usuario que mais tweetou foi: %s - %d", tweetedTheMost.getLogin(),
            tweetedTheMost.getTweets().size()));

    System.out.println(String.format("Ultimo usuario que tweetou: %s - %s", tweetOwners.get(tweetOwners.size() - 1),
        feed.get(feed.size() - 1)));
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

  public static ArrayList<String> getLastNTweetOwners(int nTweets) {
    if (tweetOwners.size() < nTweets)
      return tweetOwners;

    int startIndex = tweetOwners.size() - nTweets;
    return new ArrayList<String>(tweetOwners.subList(startIndex, tweetOwners.size()));
  }

  public static void removeTweet(String tweet, Usuario user) {
    // Função que utiliza o iterator para remover o tweet de um usuário
    Iterator<String> feedIterator = feed.iterator();
    Iterator<String> ownerIterator = tweetOwners.iterator();

    while (ownerIterator.hasNext() && feedIterator.hasNext()) {
      String feedItem = feedIterator.next();
      String owner = ownerIterator.next();

      if (owner.equals(user.getLogin()) && feedItem.equals(tweet)) {
        // Remove do arraylist da classe usuario
        user.removeTweet(tweet);
        // Remove do arraylist do feed geral de tweets
        feedIterator.remove();
        // Remove do arraylist com os donos dos tweets
        ownerIterator.remove();
        System.out.println("Tweet removido!");
        return;
      }
    }

    System.out.println("Tweet não encontrado!");
  }

  public static void removeUserTweets(Usuario user) {
    Iterator<String> feedIterator = feed.iterator();
    Iterator<String> ownerIterator = tweetOwners.iterator();

    user.removeTweets();
    while (ownerIterator.hasNext() && feedIterator.hasNext()) {
      feedIterator.next();
      String owner = ownerIterator.next();

      if (owner.equals(user.getLogin())) {
        // Remove do arraylist do feed geral de tweets
        feedIterator.remove();
        // Remove do arraylist com os donos dos tweets
        ownerIterator.remove();
      }
    }
    System.out.println("Todos os tweets do usuário foram removidos.");
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

  public static void About() {
    System.out.println("\nBem vindo ao projeto Twitter em Java");
    System.out.println("Projeto da matéria Desenvolvimento de Software da Universiade Positivo");
    System.out.println("5º período do curso de Engenharia da Computação");
    System.out.println("Realizado Por:");
    System.out.println("Bruno Henrique Miranda de Oliveira");
    System.out.println("Raul Semicek Coelho\n");
  }
}