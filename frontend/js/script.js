const API_BASE = 'http://localhost:8080/api';

async function fetchData(endpoint, elementoId) {
    const elemento = document.getElementById(elementoId);
    elemento.innerHTML = '<p>Carregando...</p>';
    
    try {
        const response = await fetch(${API_BASE}/);
        if (!response.ok) throw new Error('Erro na requisição');
        
        const data = await response.json();
        
        if (data.length === 0) {
            elemento.innerHTML = '<p>Nenhum registro encontrado.</p>';
            return;
        }
        
        elemento.innerHTML = data.map(item => 
            <div class="lista-item">
                <strong>ID:</strong> <br>
                
            </div>
        ).join('');
        
    } catch (error) {
        elemento.innerHTML = <p class="erro">Erro: </p>;
    }
}

function formatarItem(item, tipo) {
    switch(tipo) {
        case 'tutores':
            return <strong>Nome:</strong> <br>
                    <strong>CPF:</strong> <br>
                    <strong>Email:</strong> ;
        case 'pets':
            return <strong>Nome:</strong> <br>
                    <strong>Espécie:</strong> <br>
                    <strong>Raça:</strong> <br>
                    <strong>Tutor:</strong> ;
        case 'veterinarios':
            return <strong>Nome:</strong> <br>
                    <strong>CRMV:</strong> <br>
                    <strong>Especialidade:</strong> ;
        case 'consultas':
            return <strong>Data:</strong> <br>
                    <strong>Status:</strong> <br>
                    <strong>Pet:</strong> <br>
                    <strong>Veterinário:</strong> ;
        default:
            return JSON.stringify(item);
    }
}

function carregarTutores() { fetchData('tutores', 'tutores-lista'); }
function carregarPets() { fetchData('pets', 'pets-lista'); }
function carregarVeterinarios() { fetchData('veterinarios', 'veterinarios-lista'); }
function carregarConsultas() { fetchData('consultas', 'consultas-lista'); }
