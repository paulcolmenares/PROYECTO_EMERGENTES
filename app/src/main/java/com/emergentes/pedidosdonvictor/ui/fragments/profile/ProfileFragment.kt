package com.emergentes.pedidosdonvictor.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.emergentes.pedidosdonvictor.R
import com.emergentes.pedidosdonvictor.databinding.FragmentProfileBinding
import com.emergentes.pedidosdonvictor.ui.activities.login.LoginActivity
import com.emergentes.pedidosdonvictor.ui.activities.mis_pedidos.MisPedidosActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth


class ProfileFragment : Fragment() {

    private var _bindind: FragmentProfileBinding? = null
    private val binding get() = _bindind!!
    private lateinit var googleSignInClient: GoogleSignInClient
    private val CODE = 777

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        val user = FirebaseAuth.getInstance().currentUser

        Glide.with(requireContext()).load(user!!.photoUrl).into(binding.perfil)
        binding.nombre.text = user.displayName
        binding.email.text = user.email

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.web_client_ID))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        // cerrar sesion
        binding.btnCerrarSesion.setOnClickListener {

            binding.progressBarPerfil.visibility = View.VISIBLE
            binding.btnCerrarSesion.visibility = View.INVISIBLE
            val user = FirebaseAuth.getInstance().currentUser

            googleSignInClient.revokeAccess()
                .addOnCompleteListener { task: Task<Void?> ->
                    if (task.isSuccessful) {
                        user!!.delete()
                            .addOnCompleteListener {
                                val intent = Intent(requireContext(), LoginActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent)
                            }
                    } else {
                        binding.progressBarPerfil.visibility = View.GONE
                        binding.btnCerrarSesion.visibility = View.VISIBLE
                    }
                }
        }

        binding.btnMisPedidos.setOnClickListener {
            val intent = Intent(requireContext(), MisPedidosActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _bindind = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

}