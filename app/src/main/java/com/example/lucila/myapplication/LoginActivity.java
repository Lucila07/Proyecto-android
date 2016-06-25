package com.example.lucila.myapplication;

import android.annotation.TargetApi;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.example.lucila.myapplication.Datos.ServicioUsuarios;
import com.example.lucila.myapplication.Datos.ServicioUsuariosHttp;
import com.example.lucila.myapplication.Entidades.Usuario;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;

/**
 * A login screen that offers login via email/password.
 */
@TargetApi(11)
public class LoginActivity extends AppCompatActivity implements ServicioUsuariosHttp.AccesoUsuarios {

    /**
     * servicio que chequea si el usuario es valido
     */
    private ServicioUsuarios servicioUsuarios;
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;


    private Usuario usuario;

    private View mProgressView;

    private GoogleApiClient googleApiClient;
    private com.google.android.gms.common.SignInButton botonGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.



        mProgressView = findViewById(R.id.login_progress);
        //mi servicio de chequeo
        servicioUsuarios = ServicioUsuariosHttp.getInstance(this, this);
        //lo de google

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestId()
                .requestProfile()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {

                        Toast.makeText(LoginActivity.this, "Error al conectarse ", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        botonGoogle = (com.google.android.gms.common.SignInButton) findViewById(R.id.sign_in_google);
        botonGoogle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.

            GoogleSignInResult result = opr.get();
            manejarResultadoLogin(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.

            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {

                    manejarResultadoLogin(googleSignInResult);
                }
            });
        }
    }

    /**
     * se llama cuando se aprieta el boton de sign in con cuanta de google
     * carga el intent que te dice con que cuenta qrs loguearte
     */
    private void signIn() {


        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, 123);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 123) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            manejarResultadoLogin(result);
        }
    }

    private void manejarResultadoLogin(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


            usuario = new Usuario(acct.getEmail());
            usuario.setIdUsuario(acct.getId());
            usuario.setNombreApellido(acct.getDisplayName());
            usuario.setUrlFoto(acct.getPhotoUrl());
            usuario.setTelefono(" ");
            servicioUsuarios.verificarExistencia(usuario);


        } else {
            // Signed out, show unauthenticated UI.

            Toast.makeText(LoginActivity.this, "Error al conectarse con la cuenta Google ", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * override: metodo definido en acceso a usaurios, caso que el usuario existe en la bd
     * representa el chequeo asyncrono con el servidor
     * the user.
     */
    @Override
    public void cargarMain() {

        Toast.makeText(LoginActivity.this, " Bienvenido " + usuario.getNombreApellido(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * verride: metodo definido en acceso a usaurios, caso que el usuario NO existe en la bd
     * entonces carga la pantalla para que el usuario ingrese el telefono
     */
    @Override
    public void cargarTelefono() {

        Intent intent = new Intent();
        intent.setClass(LoginActivity.this, NuevoUsuarioActivity.class);
        intent.putExtra("id", usuario.getIdUsuario());
        intent.putExtra("mail", usuario.getEmail());
        intent.putExtra("nombre", usuario.getNombreApellido());
        Toast.makeText(LoginActivity.this, " Gracias por descargar nuestra APP " + usuario.getNombreApellido(), Toast.LENGTH_LONG).show();
        startActivity(intent);

    }


}