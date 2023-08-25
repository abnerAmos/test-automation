package com.example.demo.test;

import com.example.demo.config.Listener;
import com.example.demo.page.UserPage;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

@Listeners(Listener.class)
public class UserTest {

    private final UserPage userPage;

    public UserTest(UserPage userPage) {
        this.userPage = userPage;
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        Listener.testStart(
                "Usuário: " + method.getAnnotation(Test.class).testName(),
                "DESCRIÇÃO: " + method.getAnnotation(Test.class).description(),
                System.getProperty("user.home"), "Cadastro-Usuário");
    }

    @Test(testName = "Criar, editar e deletar usuário",
            description = "Deve criar, editar, atualizar senha e deletar um usuário.")
    void should_Create_New_User() {
        initInfo();

        Assert.assertTrue(userPage.isTitle("button-new", "Novo Usuário"));
        Assert.assertTrue(userPage.createAndEditAndDeleteNewUser());
    }

    @Test(testName = "Campos em branco",
            description = "Deve apresentar erros ao deixar campos obrigatórios em branco.")
    void should_Show_Fields_Error() {
        initInfo();

        Assert.assertTrue(userPage.isTitle("button-new", "Novo Usuário"));
        Assert.assertTrue(userPage.checkFieldError());
    }

    @Test(testName = "Erro ao criar ou editar usuário existente",
            description = "Deve apresentar erros ao criar ou editar usuário com nome existente.")
    void should_Show_Error_If_Register_Or_Edit_User() {
        initInfo();

        Assert.assertTrue(userPage.isTitle("button-new", "Novo Usuário"));
        Assert.assertTrue(userPage.showErrorIfRegisterUserWithEmailExisting());
    }

    @Test(testName = "Erro de senhas divergentes",
            description = "Deve apresentar erro caso usuário tente atualizar senha com senhas divergentes.")
    void should_Show_Error_If_Input_Divergent_Passords() {
        initInfo();

        Assert.assertTrue(userPage.isTitle("button-pass", "Alterando a Senha do Usuário"));
        Assert.assertTrue(userPage.showErrorIfDivergentPasswords());
    }

    @Test(testName = "Ordenação",
            description = "Deve verificar botão de ordenação.")
    void should_Check_Order_List() {
        initInfo();

        Assert.assertTrue(userPage.checkButtonAscendingList());
    }

    @Test(testName = "Permissões de usuário Not-Admin",
            description = "Deve falhar ao usuário Not-Admin tentar cadastrar, deletar, editar senha ou deletar usuário.")
    void should_Fail_If_User_Not_Admin_Try_Register_Or_Edit_Or_Edit_Pass_Or_Delete() {
        initInfo();

        Assert.assertTrue(userPage.nonAdminUserCantRegisterOrEditOrEditPassOrDeleteUser());
    }

    @AfterMethod
    public void afterMethod() {
        userPage.closeBrowser();
    }

    void initInfo() {
        Listener.logTest("info", "ACESSANDO A PÁGINA: " + "Cadastro de Usuários.");
        Listener.logTest("info", "TIPO DE USUÁRIO: " + "Administrador Interno.");
        Assert.assertTrue(userPage.isCurrentPage());
    }
}
