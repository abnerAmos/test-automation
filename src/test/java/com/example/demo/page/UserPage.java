package com.example.demo.page;

import com.example.demo.config.Listener;
import com.example.demo.config.PageObject;
import com.example.demo.service.UserService;
import com.example.demo.util.CommonUtil;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.Objects;

public class UserPage extends PageObject {

    private static final CommonUtil util = new CommonUtil();
    private static final UserService userService = new UserService();

    public UserPage(WebDriver browser) {
        super(browser);
    }

    @Override
    public UserPage goToThisPage() {
        return new UserPage(browser);
    }

    // --------------------------------------> Teste Usuário Humano

    public boolean createAndEditAndDeleteNewUser() {
        createNewUser(null, null);

        util.filterByThisString(browser, "Fulano de Tal");
        if (!isTitle("button-edit", "Editando Usuário"))
            return false;

        try {
            userService.fillField(browser, "txt-name", "Sicrano da Silva");
            userService.fillField(browser, "txt-username", "sicrano@email");
            userService.fillField(browser, "txt-email", "sicrano@email");
            util.clickButtonByCssSelector(browser, "#chk-is-admin span");
            util.clickButtonByCssSelector(browser, "#chk-is-matrix span");
            util.clickButtonById(browser, "button-save");

            Listener.logTest("pass", "SUCESSO :: O usuário foi editado!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel atualizar o usuário.");
            e.printStackTrace();
            return false;
        }

        if (!userService.checkTextReturn(browser, "show-dialog", "O usuário foi atualizado com sucesso!"))
            return false;

        util.clickButtonById(browser, "button-ok");
        if (!editPass("Sicrano da Silva"))
            return false;

        return deleteUser("Sicrano da Silva");
    }

    public boolean createNewUser(String name, String email) {
        try {
            if (name == null && email == null) {
                userService.fillField(browser, "txt-name", "Fulano de Tal");
                userService.fillField(browser, "txt-username", "fulano@email");
                userService.fillField(browser, "txt-email", "fulano@email");
                userService.fillField(browser, "txt-password", "654321");
                userService.fillField(browser, "txt-password-confirm", "654321");
                util.clickButtonById(browser, "type-user");
                userService.selectType(browser, "HUMAN");
                util.clickButtonByCssSelector(browser, "#chk-is-admin span");
                util.clickButtonByCssSelector(browser, "#chk-is-matrix span");
                util.clickButtonById(browser, "button-save");
            } else {
                userService.fillField(browser, "txt-name", name);
                userService.fillField(browser, "txt-username", email);
                userService.fillField(browser, "txt-email", email);
                userService.fillField(browser, "txt-password", "654321");
                userService.fillField(browser, "txt-password-confirm", "654321");
                util.clickButtonById(browser, "type-user");
                userService.selectType(browser, "HUMAN");
                util.clickButtonByCssSelector(browser, "#chk-is-admin span");
                util.clickButtonByCssSelector(browser, "#chk-is-matrix span");
                util.clickButtonById(browser, "button-save");
            }

            Listener.logTest("pass", "SUCESSO :: O usuário foi cadastrado!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel cadastrar o usuário.");
            e.printStackTrace();
            return false;
        }

        if (!userService.checkTextReturn(browser, "show-dialog", "O usuário foi cadastrado com sucesso!"))
            return false;

        util.clickButtonById(browser, "button-ok");
        return true;
    }

    public boolean editPass(String typeUser) {

        try {
            util.filterByThisString(browser, typeUser);
            if (!isTitle("button-pass", "Alterando a Senha do Usuário"))
                return false;

            userService.fillField(browser, "txt-password", "123456");
            userService.fillField(browser, "txt-password-confirm", "123456");
            util.clickButtonById(browser, "button-save");

            Listener.logTest("pass", "SUCESSO :: A senha do usuário foi atualizada!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel atualizar a senha do usuário.");
            e.printStackTrace();
            return false;
        }

        if (!userService.checkTextReturn(browser, "show-dialog", "O usuário foi atualizado com sucesso!"))
            return false;

        util.clickButtonById(browser, "button-ok");
        return true;
    }

    public boolean deleteUser(String typeUser) {
        try {
            util.filterByThisString(browser, Objects.requireNonNullElse(typeUser, "Fulano de Tal"));

            if (!isTitle("button-delete", "Você tem certeza que deseja excluir?"))
                return false;

            util.clickButtonById(browser, "yes-button");
            if (!userService.checkTextReturn(browser, "show-dialog", "O usuário foi excluído com sucesso!"))
                return false;

            util.clickButtonById(browser, "button-ok");
            if (!userService.isDeleted(browser, "no-item", "Não há usuários."))
                return false;

            Listener.logTest("pass", "SUCESSO :: O usuário foi excluído!");
            return true;
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar se o usuário foi excluído.");
            e.printStackTrace();
            return false;
        }
    }

    // --------------------------------------> Test User Page Errors

    public boolean checkFieldError() {
        try {
            if (!checkDivergentPass())
                return false;

            if (!userService.forceErrorField(browser, "txt-name", "name-error", "Campo Obrigatório"))
                return false;

            if (!userService.forceErrorField(browser, "txt-username", "username-error",
                    "Campo obrigatório, insira um e-mail válido."))
                return false;

            if (!userService.forceErrorField(browser, "txt-email", "email-error",
                    "Campo obrigatório, insira um e-mail válido."))
                return false;

            if (!userService.forceErrorField(browser, "txt-password", "pass-error", "Campo Obrigatório"))
                return false;

            if (!userService.forceErrorField(browser, "txt-password-confirm", "confirm-pass-error", "Campo Obrigatório"))
                return false;

            Listener.logTest("pass", "SUCESSO :: Cadastro de usuário validando obrigatoriedade de preenchimento de campos vazios!");
            return true;
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar a obrigatoriedade de preenchimento de campos vazios.");
            e.printStackTrace();
            return false;
        }
    }

    public boolean checkDivergentPass() {
        try {
            userService.fillField(browser, "txt-name", "Fulano de Tal");
            userService.fillField(browser, "txt-username", "fulano@email");
            userService.fillField(browser, "txt-email", "fulano@email");
            userService.fillField(browser, "txt-password", "123");
            userService.fillField(browser, "txt-password-confirm", "456");

            util.clickButtonById(browser, "button-save");

            if (!userService.checkTextReturn(browser, "show-dialog",
                    "As senhas inseridas não conferem! Por favor, verifique os dados inseridos!"))
                return false;

            Listener.logTest("pass", "SUCESSO :: Usuário não cadastrou com senhas divergentes!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar se usuário cadastrou com senhas divergentes.");
            e.printStackTrace();
            return false;
        }

        util.clickButtonById(browser, "button-ok");
        return true;
    }

    public boolean showErrorIfRegisterUserWithEmailExisting() {
        createNewUser(null, null);

        try {
            if (!isTitle("button-new", "Novo Usuário"))
                return false;

            userService.fillField(browser, "txt-name", "Fulano de Tal");
            userService.fillField(browser, "txt-username", "fulano@email");
            userService.fillField(browser, "txt-email", "fulano@email");
            userService.fillField(browser, "txt-password", "123456");
            userService.fillField(browser, "txt-password-confirm", "123456");
            util.clickButtonById(browser, "button-save");

            if (!userService.checkTextReturn(browser, "show-dialog",
                    "O usuário já foi utilizado no sistema por esta ou outra empresa! Por favor verifique os dados inseridos!"))
                return false;

            Listener.logTest("pass", "SUCESSO :: Usuário não cadastrou com nome duplicado!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar se usuário está cadastrando itens com campos vazios.");
            e.printStackTrace();
            return false;
        }

        util.clickButtonById(browser, "button-ok");
        util.clickButtonById(browser, "button-cancel");

        return showErrorIfEditUserWithEmailExisting();
    }

    public boolean showErrorIfEditUserWithEmailExisting() {
        if (!isTitle("button-new", "Novo Usuário"))
            return false;

        createNewUser("Sicrano da Silva", "sicrano@email");

        try {
            util.filterByThisString(browser, "Sicrano da Silva");

            if (!isTitle("button-edit", "Editando Usuário"))
                return false;

            userService.fillField(browser, "txt-name", "Fulano de Tal");
            userService.fillField(browser, "txt-username", "fulano@email");
            userService.fillField(browser, "txt-email", "fulano@email");
            util.clickButtonById(browser, "button-save");

            if (!userService.checkTextReturn(browser, "show-dialog",
                    "O usuário já foi utilizado no sistema por esta ou outra empresa! Por favor verifique os dados inseridos!"))
                return false;

            Listener.logTest("pass", "SUCESSO :: Usuário não editou com nome duplicado!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: c está editando itens com campos vazios.");
            e.printStackTrace();
            return false;
        }

        util.clickButtonById(browser, "button-ok");
        util.clickButtonById(browser, "button-cancel");

        if (!deleteUser("Fulano de Tal"))
            return false;

        return deleteUser("Sicrano da Silva");
    }

    public boolean showErrorIfDivergentPasswords() {
        try {
            userService.fillField(browser, "txt-password", "123");
            userService.fillField(browser, "txt-password-confirm", "456");
            util.clickButtonById(browser, "button-save");

            if (!userService.checkTextReturn(browser, "show-dialog",
                    "As senhas inseridas não conferem! Por favor, verifique os dados inseridos!"))
                return false;

            Listener.logTest("pass", "SUCESSO :: Usuário não alterou senha com senhas divergentes!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar se usuário está alterando senha com senhas divergentes.");
            e.printStackTrace();
            return false;
        }

        util.clickButtonById(browser, "button-ok");
        util.clickButtonById(browser, "button-cancel");
        return true;
    }

    // --------------------------------------> Test User Page - User Not Admin

    public boolean nonAdminUserCantRegisterOrEditOrEditPassOrDeleteUser() {
        if (!userService.checkStringValue(browser, "logged-user").contains("Usuário Selenium NÃO ADMIN")) {
            Listener.logTest("fail", "ERRO :: Usuário logado diferente do esperado: Usuário Selenium NÃO ADMIN");
            return false;
        }

        Listener.logTest("pass", "SUCESSO :: Usuário Not-Admin efetuou loggin!");
        return nonAdminUserCannotRegisterNewUser();
    }

    public boolean nonAdminUserCannotRegisterNewUser() {
        try {
            if (!isTitle("button-new", "Novo Usuário"))
                return false;

            userService.fillField(browser, "txt-name", "Selenium Test");
            userService.fillField(browser, "txt-username", "mock@test");
            userService.fillField(browser, "txt-email", "mock@test");
            userService.fillField(browser, "txt-password", "123456");
            userService.fillField(browser, "txt-password-confirm", "123456");
            util.clickButtonById(browser, "button-save");

            if (!userService.checkTextReturn(browser, "show-dialog", "Esse perfil não tem permissão para realizar esta operação!"))
                return false;

            Listener.logTest("pass", "SUCESSO :: Usuário Not-Admin não tem permissão para cadastrar um usuário!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar permissão do usuário Not-Admin para cadastrar um usuário.");
            e.printStackTrace();
            return false;
        }

        util.clickButtonById(browser, "button-ok");
        return nonAdminUserCannotEditUserExisting();
    }

    public boolean nonAdminUserCannotEditUserExisting() {
        try {
            util.filterByThisString(browser, "Fulano de Tal");
            if (!isTitle("button-edit", "Editando Usuário"))
                return false;

            userService.fillField(browser, "txt-name", "EDITADO");
            userService.fillField(browser, "txt-username", "mock@editado");
            userService.fillField(browser, "txt-email", "mock@editado");
            util.clickButtonById(browser, "button-save");

            if (!userService.checkTextReturn(browser, "show-dialog",
                    "Esse perfil não tem permissão para realizar esta operação!"))
                return false;

            Listener.logTest("pass", "SUCESSO :: Usuário Not-Admin não tem permissão para editar outro usuário!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar permissão do usuário Not-Admin para editar outro usuário.");
            e.printStackTrace();
            return false;
        }

        util.clickButtonById(browser, "button-ok");
        return nonAdminUserCannotEditPassword();
    }

    public boolean nonAdminUserCannotEditPassword() {
        try {
            util.filterByThisString(browser, "Fulano de Tal");
            if (!isTitle("button-pass", "Alterando a Senha do Usuário"))
                return false;

            userService.fillField(browser, "txt-password", "123456");
            userService.fillField(browser, "txt-password-confirm", "123456");
            util.clickButtonById(browser, "button-save");

            Listener.logTest("pass", "SUCESSO :: Usuário Not-Admin não tem permissão para alterar a senha de outro usuário!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar permissão do usuário Not-Admin para alterar a senha de outro usuário.");
            e.printStackTrace();
            return false;
        }

        if (!userService.checkTextReturn(browser, "show-dialog",
                "Esse perfil não tem permissão para realizar esta operação!"))
            return false;

        util.clickButtonById(browser, "button-ok");
        return nonAdminUserCannotDeleteUserExisting();
    }

    public boolean nonAdminUserCannotDeleteUserExisting() {
        try {
            if (!isTitle("button-delete", "Você tem certeza que deseja excluir?"))
                return false;

            util.clickButtonById(browser, "yes-button");
            if (!userService.checkTextReturn(browser, "show-dialog",
                    "Esse perfil não tem permissão para realizar esta operação!"))
                return false;

            Listener.logTest("pass", "SUCESSO :: Usuário Not-Admin não tem permissão para deletar outro usuário!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar permissão do usuário Not-Admin para deletar outro usuário.");
            e.printStackTrace();
            return false;
        }

        util.clickButtonById(browser, "button-ok");
        return nonAdminCanEditOwnPassword();
    }

    public boolean nonAdminCanEditOwnPassword() {
        String currentUser = userService.checkStringValue(browser, "logged-user");

        try {
            util.filterByThisString(browser, currentUser);
            if (!isTitle("button-pass", "Alterando a Senha do Usuário"))
                return false;

            userService.fillField(browser, "txt-password", "123456");
            userService.fillField(browser, "txt-password-confirm", "123456");
            util.clickButtonById(browser, "button-save");

            if (!userService.checkTextReturn(browser, "show-dialog", "O usuário foi atualizado com sucesso!"))
                return false;

            Listener.logTest("pass", "SUCESSO :: Usuário Not-Admin possuí permissão para alterar a própria senha!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar permissão do usuário Not-Admin para alterar a própria senha.");
            e.printStackTrace();
            return false;
        }

        util.clickButtonById(browser, "button-ok");
        return true;
    }

    // --------------------------------------> Test Ordenation

    public boolean checkButtonAscendingList() {
        try {
            if (!populateForTestOrder())
                return false;

            List<String> names = userService.isListNamesInOrder(browser, false);
            util.clickButtonById(browser, "sort-name");
            List<String> namesOrder = userService.isListNamesInOrder(browser, true);

            if (!names.equals(namesOrder))
                return false;

            Listener.logTest("pass", "SUCESSO :: Ordenação funcionando corretamente!");
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel validar ordenação de usuários.");
            e.printStackTrace();
            return false;
        }

        if (!deleteUser("Fulano de Tal" + "A"))
            return false;

        return deleteUser("Fulano de Tal" + "B");
    }

    public boolean populateForTestOrder() {
        try {
            if (!isTitle("button-new", "Novo Usuário"))
                return false;

            userService.fillField(browser, "txt-name", "Fulano de Tal" + "B");
            userService.fillField(browser, "txt-username", "Fulano de Tal" + "@b");
            userService.fillField(browser, "txt-email", "Fulano de Tal" + "@b");
            userService.fillField(browser, "txt-password", "123");
            userService.fillField(browser, "txt-password-confirm", "123");
            util.clickButtonById(browser, "button-save");
            util.clickButtonById(browser, "button-ok");

            if (!isTitle("button-new", "Novo Usuário"))
                return false;

            userService.fillField(browser, "txt-name", "Fulano de Tal" + "A");
            userService.fillField(browser, "txt-username", "Fulano de Tal" + "@a");
            userService.fillField(browser, "txt-email", "Fulano de Tal" + "@a");
            userService.fillField(browser, "txt-password", "456");
            userService.fillField(browser, "txt-password-confirm", "456");
            util.clickButtonById(browser, "button-save");
            util.clickButtonById(browser, "button-ok");

            Listener.logTest("pass", "SUCESSO :: Usuário populado!");
            return true;
        } catch (Exception e) {
            Listener.logTest("fail", "ERRO :: Não foi possivel popular Usuário para teste de ordenação.");
            e.printStackTrace();
            return false;
        }
    }

    // --------------------------------------> Check Return Messages

    public boolean isTitle(String id, String text) {
        if (id.equals("button-new"))
            util.clickButtonById(browser, id);
        if (id.equals("button-edit"))
            userService.findButton(browser, id);
        if (id.equals("button-pass"))
            userService.findButton(browser, id);
        if (id.equals("button-delete"))
            userService.findButton(browser, id);
        return userService.checkModal(browser, text);
    }

    public boolean isCurrentPage() {
        return userService.isCurrentPage(browser);
    }
}
