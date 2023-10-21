// Model (Contract)
public interface UserRepository {
    void saveUser(User user);
    User getUser(String username);
}

// View (Contract)
public interface LoginView {
    void showSuccessMessage();
    void showErrorMessage();
    String getUsername();
    String getPassword();
}

// Presenter
public class LoginPresenter {
    private UserRepository userRepository;
    private LoginView loginView;

    public LoginPresenter(UserRepository userRepository, LoginView loginView) {
        this.userRepository = userRepository;
        this.loginView = loginView;
    }

    public void login() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        User user = userRepository.getUser(username);

        if (user != null && user.getPassword().equals(password)) {
            loginView.showSuccessMessage();
        } else {
            loginView.showErrorMessage();
        }
    }
}

// Implementation of UserRepository
public class UserRepositoryImpl implements UserRepository {
    private Map<String, User> users = new HashMap<>();

    @Override
    public void saveUser(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User getUser(String username) {
        return users.get(username);
    }
}

// Implementation of LoginView (Example: Android Activity)
public class LoginActivity extends AppCompatActivity implements LoginView {
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        usernameEditText = findViewById(R.id.editText_username);
        passwordEditText = findViewById(R.id.editText_password);
        loginButton = findViewById(R.id.button_login);

        // Create presenter with repository and view
        UserRepository userRepository = new UserRepositoryImpl();
        loginPresenter = new LoginPresenter(userRepository, this);

        // Set click listener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginPresenter.login();
            }
        });
    }

    @Override
    public void showSuccessMessage() {
        Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public String getUsername() {
        return usernameEditText.getText().toString();
    }

    @Override
    public String getPassword() {
        return passwordEditText.getText().toString();
    }
}

// Model class
public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
