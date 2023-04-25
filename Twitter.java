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
          signOut();
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
    String nome = nextLine("Nome: ", false);
    String login = nextLine("Login: ", false);
    String email = nextLine("Email: ", false);
    String senha = nextLine("Senha: ", false);

    // Verifica se o usuário digitado já existe
    if (findUser(login, false) != null || findUserByEmail(email, false) != null) {
      System.out.println("Usuario ja existente!");
      return;
    }

    // Guardando esse usuário no arraylist users
    users.add(new Usuario(nome, login, email, senha));
    System.out.println("Usuário cadastrado :)");
  }

  public static void printUsers() {
    if (users.size() == 0) {
      System.out.println("Não há nenhum usuário!");
      return;
    }

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
      user.signIn(user.getLogin(), nextLine("Senha: ", false));
    else
      System.out.println("Usuário já logado!!");
  }

  public static void signOut() {
    Usuario user = findUser(true);

    if (user == null)
      return;

    if (user.isLogged())
      user.signOut();
    else
      System.out.println("Usuário já deslogado!!");
  }

  public static void tweet() {
    Usuario user = findUser(true);

    if (user == null)
      return;

    // Verifica se o usuário esta logado
    if (user.isLogged()) {
      String tweet = nextLine("Tweet: ", true);
      // Verifica se o tweet foi salvo no arraylist de de tweets do usuário
      if (user.tweet(tweet)) {
        tweetOwners.add(user.getLogin());
        feed.add(tweet);
      }
    } else {
      System.out.println("Primeiro realize o login!");
    }
  }

  public static void printLastNTweets() {
    int nTweets = nextInt("Quantos tweets deseja ver? ");
    ArrayList<String> lastNTweets = getLastNTweets(nTweets);
    ArrayList<String> lastNTweetOwners = getLastNTweetOwners(nTweets);
    int lastNTweetsSize = lastNTweets.size();

    if (lastNTweetsSize == 0) {
      System.out.println("Não há nenhum tweet ainda!");
      return;
    }

    for (int i = 0; i < lastNTweetsSize; i++) {
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

    if (userTweets.size() == 0) {
      System.out.println("Este usuário não tem nenhum tweet.");
      return;
    }

    System.out.println("Selecione um tweet: ");

    int userTweetsSize = userTweets.size();
    for (int i = 0; i < userTweetsSize; i++) {
      System.out.println(String.format("%d - %s", i + 1, userTweets.get(i)));
    }

    int tweetNumber = nextInt();

    while (tweetNumber <= 0 || tweetNumber > userTweetsSize) {
      System.out.print("Valor inválido, digite novamente ou zero para cancelar: ");
      tweetNumber = nextInt();

      if (tweetNumber == 0)
        return;
    }

    removeTweet(userTweets.get(tweetNumber - 1), user);
  }

  public static void changePassword() {
    Usuario user = findUser(true);

    if (user != null)
      user.changePassword(nextLine("Senha antiga: ", false), nextLine("Senha nova: ", false));
  }

  public static void removeUser() {
    Iterator<Usuario> userIterator = users.iterator();
    Usuario user = findUser(true);

    if (user == null)
      return;

    if (!user.isValidPassword(nextLine("Digite a senha do usuario: ", false)))
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
    if (users.size() > 0) {
      System.out.println("Número de tweets por usuário: ");
      for (Usuario user : users) {
        System.out.println(user.printTweets());
      }

      if (feed.size() > 0) {
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
      } else {
        System.out.println("Usuario que mais tweetou foi: Não há tweets.");
        System.out.println("último usuario que tweetou: Não há tweets.");
      }
    } else {
      System.out.println("Número de tweets por usuário: 0");
      System.out.println("Usuario que mais tweetou foi: Não há tweets.");
      System.out.println("último usuario que tweetou: Não há tweets.");
    }
  }
  // #endregion

  // #region "Api"
  public static Usuario findUser(boolean showMessage) {
    String login = nextLine("Login do usuário: ", false);
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

  public static Usuario findUserByEmail(boolean showMessage) {
    String email = nextLine("Email do usuário: ", false);
    return findUserByEmail(email, showMessage);
  }

  public static Usuario findUserByEmail(String email, boolean showMessage) {
    for (Usuario user : users) {
      if (user.getEmail().equals(email)) {
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
  public static String nextLine(String message, boolean jumpLine) {
    if (jumpLine)
      System.out.println(message);
    else
      System.out.print(message);

    return nextLine();
  }

  public static String nextLine() {
    return input.nextLine();
  }

  public static int nextInt(String message) {
    System.out.print(message);
    return nextInt();
  }

  public static int nextInt() {
    try {
      int number = input.nextInt();
      input.nextLine();
      return number;
    } catch (Exception ex) {
      input.nextLine();
      return nextInt("Você deve digitar um número inteiro: ");
    }
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