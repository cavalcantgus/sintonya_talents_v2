package com.example.demo.enums;

public enum EnumPermissions {
    // Profile
    PROFILE_READ, //Leitura de perfil público
    PROFILE_READ_BY_ID, // Leitura de perfil específico por id
    PROFILE_READ_DETAILS, // Leitura dos detalhes de um perfil
    PROFILE_UPDATE, // Atualização de um perfil

    // Candidate
    CANDIDATE_READ, // Leitura dos dados de um candidato
    CANDIDATE_READ_BY_ID, // Leitura de um candidato específico por id
    CANDIDATE_READ_DETAILS, // Leitura dos detalhes de um candidato

    // Enterprise
    ENTERPRISE_READ, // Leitura dos dados de uma empresa
    ENTERPRISE_READ_BY_ID, // Leitura de uma empresa específica por id
    ENTERPRISE_READ_DETAILS, // Leitura dos detalhes de uma empresa

    // User
    USER_READ, // Leitura dos dados de um usuário
    USER_READ_BY_ID, // Leitura de um usuário com base no id
    USER_READ_DETAILS, // Leitura dos detalhes de um usuário

    // Vacancy
    VACANCY_READ, // Leitura dos dados de vaga
    VACANCY_READ_BY_ID, // Leitura dos dados de uma vaga com base no id
    VACANCY_READ_DETAILS, // Leitura dos detalhes de uma vaga
    VACANCY_CREATE, // Criação de vagas
    VACANCY_UPDATE, // Atualização de vaga

    // Job Application
    JOB_APPLICATION_READ, // Leitura de uma candidatura
    JOB_APPLICATION_READ_BY_ID, // Leitura de uma candidatura com base no id
    JOB_APPLICATION_READ_DETAILS, // Leitura dos detalhes de uma candidatura
    JOB_APPLICATION_APPLY, // Aplicação pra uma vaga

    // Selection Process
    SELECTION_PROCESS_READ, // Leitura de um processo seletivo
    SELECTION_PROCESS_READ_BY_ID, // Leitura de um processo seletivo com base no id
    SELECTION_PROCESS_READ_DETAILS, // Leitura dos detalhes de um processo seletivo

    // Skill
    SKILL_READ, // Leitura das skills
    SKILL_CREATE, // Criação de skilss
    SKILL_UPDATE, // Atualização de skill

    // Certificate
    CERTIFICATE_READ, // Leitura de um certificado
    CERTIFICATE_CREATE, // Criação de um certificado
    CERTIFICATE_UPDATE, // Atualização de um certificado
    CERTIFICATE_DELETE, // Deleção de um certificado

    // Experience
    EXPERIENCE_READ, // Leitura de uma experiência
    EXPERIENCE_CREATE, // Criação de uma experiência
    EXPERIENCE_UPDATE, // Atualização de uma experiência
    EXPERIENCE_DELETE, // Deleção de uma experiência

    // Post
    POST_READ, // Leitura de um post
    POST_READ_BY_ID, // Leitura de um post com base num id
    POST_CREATE, // Criação de um Post
    POST_UPDATE, // Atualização de um Post
    POST_DELETE, // Deleção de um post

    ADMIN_ACCESS,
}
