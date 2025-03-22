//package com.cabral.hubsrc.source.remote.services
//
//import com.cabral.arch.extensions.UserThrowable
//import com.cabral.core.common.domain.model.User
//import com.cabral.core.common.domain.model.UserRegister
//import com.cabral.hubsrc.source.DBConstants
//import com.cabral.hubsrc.source.remote.services.UserServicesImpl
//import com.google.android.gms.tasks.Task
//import com.google.firebase.auth.AuthResult
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.auth.FirebaseUser
//import com.google.firebase.firestore.CollectionReference
//import com.google.firebase.firestore.DocumentReference
//import com.google.firebase.firestore.DocumentSnapshot
//import com.google.firebase.firestore.FirebaseFirestore
//import com.google.firebase.firestore.QuerySnapshot
//import io.mockk.coEvery
//import io.mockk.coVerify
//import io.mockk.every
//import io.mockk.mockk
//import junit.framework.TestCase.assertEquals
//import kotlinx.coroutines.ExperimentalCoroutinesApi
//import kotlinx.coroutines.flow.toList
//import kotlinx.coroutines.tasks.await
//import kotlinx.coroutines.test.runTest
//import org.junit.Before
//import org.junit.Test
//import kotlin.test.DefaultAsserter.fail
//@ExperimentalCoroutinesApi
//class UserServicesImplTest {
//
//    private val db: FirebaseFirestore = mockk(relaxed = true)
//    private val auth: FirebaseAuth = mockk(relaxed = true)
//    private val collectionReference: CollectionReference = mockk(relaxed = true)
//    private val querySnapshot: QuerySnapshot = mockk(relaxed = true)
//    private val documentReference: DocumentReference = mockk(relaxed = true)
//    private val task: Task<QuerySnapshot> = mockk(relaxed = true)
//
//    private lateinit var userServices: UserServicesImpl
//
//    @Before
//    fun setUp() {
//        userServices = UserServicesImpl(db, auth)
//
//        // Mockando o comportamento do FirebaseFirestore
//        every { db.collection(DBConstants.USER) } returns collectionReference
//        every { collectionReference.document(any()) } returns documentReference
//        every { collectionReference.whereEqualTo(DBConstants.EMAIL, any()) } returns collectionReference
//        every { collectionReference.get() } returns task
//    }
//
//    @Test
//    fun `addUser should register user if not already registered`() = runTest {
//        val user = User("john@example.com", "password123")
//        val userRegister = UserRegister("John", "john@example.com")
//
//        // Mockando o comportamento de consulta do Firestore
//        every { task.isComplete } returns true
//        every { task.isSuccessful } returns true
//        every { task.result } returns querySnapshot
//        every { querySnapshot.isEmpty } returns true
//
//        // Criando o mock do Task<AuthResult> para o Firebase Auth
//        val authResult: AuthResult = mockk()
//        val taskAuthResult: Task<AuthResult> = mockk()
//        val firebaseUser: FirebaseUser = mockk()
//
//        // Configurando o retorno do Task<AuthResult> no Firebase Auth
//        every { auth.createUserWithEmailAndPassword(user.email!!, user.password!!) } returns taskAuthResult
//        /*
//        every { taskAuthResult.isComplete } returns true
//        every { taskAuthResult.isSuccessful } returns true
//        every { taskAuthResult.result } returns authResult
//        every { authResult.user } returns firebaseUser
//
//        // Mockando o Task<Void> do sendEmailVerification() para simular sucesso
//        val taskVoid: Task<Void> = mockk()
//        every { firebaseUser.sendEmailVerification() } returns taskVoid
//        every { taskVoid.isComplete } returns true
//        every { taskVoid.isSuccessful } returns true
//
//        // Mockando o comportamento de setar o usuário no Firestore
//        coEvery { documentReference.set(userRegister) } returns mockk()
//
//        // Chama o método addUser
//        val result = userServices.addUser(user).toList()
//
//        assertEquals(Unit, result[0])
//
//         */
//    }
//
//
//
//
//    @Test
//    fun `addUser should throw exception if user already exists`() = runTest {
//        val user = User("john@example.com", "password123")
//
//        // Simulando que o usuário já existe
//        every { task.isComplete } returns true
//        every { task.isSuccessful } returns true
//        every { task.result } returns querySnapshot
//        every { querySnapshot.isEmpty } returns false
//
//        try {
//            userServices.addUser(user).toList()
//            fail("Expected UserAlreadyRegisterPasswordThrowable")
//        } catch (e: UserThrowable.UserAlreadyRegisterPasswordThrowable) {
//            // Success, exception thrown as expected
//        }
//    }
//
////    @Test
////    fun `login should return user when email is verified`() = runTest {
////        val user = User("john@example.com", "password123")
////        val signInResult: AuthResult = mockk(relaxed = true)
////        val firebaseUser: FirebaseUser = mockk(relaxed = true)
////
////        every { auth.signInWithEmailAndPassword(user.email!!, user.password!!).result } returns signInResult
////        every { signInResult.user } returns firebaseUser
////        every { firebaseUser?.isEmailVerified } returns true
////
////        every { task.isComplete } returns true
////        every { task.isSuccessful } returns true
////        every { task.result } returns querySnapshot
////        every { querySnapshot.documents.isNotEmpty() } returns true
////
////        coEvery { documentReference.get() } returns task
////
////        val result = userServices.login(user).toList()
////
////        assertEquals(user, result[1])
////    }
//
//    @Test
//    fun `login should throw exception when email is not verified`() = runTest {
//        val user = User("john@example.com", "password123")
//        val signInResult: AuthResult = mockk(relaxed = true)
//        val firebaseUser: FirebaseUser = mockk(relaxed = true)
//
//        every { auth.signInWithEmailAndPassword(user.email!!, user.password!!).result } returns signInResult
//        every { signInResult.user } returns firebaseUser
//        every { firebaseUser?.isEmailVerified } returns false
//        coEvery { firebaseUser?.sendEmailVerification()?.await() } returns mockk(relaxed = true)
//
//        try {
//            userServices.login(user).toList()
//            fail("Expected CheckEmailThrowable")
//        } catch (e: UserThrowable.CheckEmailThrowable) {
//            // Success, exception thrown as expected
//        }
//    }
//
////    @Test
////    fun `autoLogin should return user when key exists`() = runTest {
////        val key = "some-user-key"
////        val user = User("john@example.com", key = key)
////
////        every { db.collection(DBConstants.USER).document(key).get() } returns task
////        every { task.isComplete } returns true
////        every { task.isSuccessful } returns true
////        every { task.result } returns querySnapshot
////        every { querySnapshot.toObjects(User::class.java) } returns user
////
////        val result = userServices.autoLogin(key).toList()
////
////        assertEquals(user, result[1])
////    }
//
//    @Test
//    fun `googleLogin should return existing user when found`() = runTest {
//        val email = "john@example.com"
//        val name = "John"
//        val user = User(email, key = "some-user-key")
//
//        every { task.isComplete } returns true
//        every { task.isSuccessful } returns true
//        every { task.result } returns querySnapshot
//        every { querySnapshot.isEmpty } returns false
//
//        coEvery { collectionReference.get() } returns task
//
//        val result = userServices.googleLogin(email, name).toList()
//
//        assertEquals(user, result[1])
//    }
//
//    @Test
//    fun `forgotPassword should send reset email`() = runTest {
//        val email = "john@example.com"
//
//        coEvery { auth.sendPasswordResetEmail(email).await() } returns mockk(relaxed = true)
//
//        userServices.forgotPassword(email)
//
//        coVerify { auth.sendPasswordResetEmail(email).await() }
//    }
//}
