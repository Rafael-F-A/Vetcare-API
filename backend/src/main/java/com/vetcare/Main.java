package com.vetcare;

import com.vetcare.model.entity.Tutor;
import com.vetcare.model.entity.Pet;
import com.vetcare.model.enums.EspeciePet;
import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== VETCARE API ===\n");
        System.out.println("Sistema de gestão de clínica veterinária");
        System.out.println("Versão: 1.0.0\n");
        
        Tutor tutor = new Tutor();
        tutor.setId(1L);
        tutor.setNome("João Silva");
        tutor.setCpf("123.456.789-00");
        tutor.setEmail("joao@email.com");
        tutor.setTelefone("(11) 99999-9999");
        tutor.setEndereco("Rua das Flores, 123 - São Paulo/SP");
        tutor.setDataCadastro(LocalDate.now());
        
        Pet pet = new Pet();
        pet.setId(1L);
        pet.setNome("Rex");
        pet.setEspecie(EspeciePet.CÃO);
        pet.setRaca("Labrador");
        pet.setCor("Caramelo");
        pet.setPeso(25.5);
        pet.setDataNascimento(LocalDate.of(2020, 5, 15));
        pet.setTutor(tutor);
        
        System.out.println("DADOS CADASTRADOS");
        System.out.println("\nDados do Tutor:");
        System.out.println("  ID: " + tutor.getId());
        System.out.println("  Nome: " + tutor.getNome());
        System.out.println("  CPF: " + tutor.getCpf());
        System.out.println("  Email: " + tutor.getEmail());
        System.out.println("  Telefone: " + tutor.getTelefone());
        System.out.println("  Endereço: " + tutor.getEndereco());
        System.out.println("  Data Cadastro: " + tutor.getDataCadastro());
        
        System.out.println("\nDados do Pet:");
        System.out.println("  ID: " + pet.getId());
        System.out.println("  Nome: " + pet.getNome());
        System.out.println("  Espécie: " + pet.getEspecie());
        System.out.println("  Raça: " + pet.getRaca());
        System.out.println("  Cor: " + pet.getCor());
        System.out.println("  Peso: " + pet.getPeso() + " kg");
        System.out.println("  Data Nascimento: " + pet.getDataNascimento());
        System.out.println("  Tutor: " + pet.getTutor().getNome());
        
        System.out.println("\n--- SISTEMA INICIALIZADO COM SUCESSO ---");
    }
}