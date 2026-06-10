// API Configuration
const API_BASE = 'http://localhost:8081/api';

// State
let currentModule = 'dashboard';
let editingId = null;
let currentEntity = null;

// Options
const ESPECIES = ['CÃO', 'GATO', 'PÁSSARO', 'ROEDOR', 'RÉPTIL', 'OUTRO'];
const STATUS_CONSULTA = ['AGENDADA', 'REALIZADA', 'CANCELADA', 'NAO_COMPARECEU'];

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    setupNavigation();
    loadDashboard();
});

// Navigation
function setupNavigation() {
    document.querySelectorAll('.nav-item').forEach(btn => {
        btn.addEventListener('click', () => {
            const module = btn.dataset.module;
            switchModule(module);
        });
    });
}

function switchModule(module) {
    currentModule = module;
    
    document.querySelectorAll('.nav-item').forEach(btn => {
        btn.classList.remove('active');
        if (btn.dataset.module === module) {
            btn.classList.add('active');
        }
    });
    
    document.querySelectorAll('.module').forEach(mod => {
        mod.classList.remove('active');
    });
    document.getElementById(module).classList.add('active');
    
    const titles = {
        dashboard: 'Dashboard',
        tutores: 'Tutores',
        pets: 'Pets',
        veterinarios: 'Veterinários',
        consultas: 'Consultas',
        agendas: 'Agendas'
    };
    
    const titleElement = document.getElementById('page-title');
    titleElement.textContent = titles[module] || module;
    
    if (module !== 'dashboard') {
        loadList(module);
    } else {
        loadDashboard();
    }
}

// Load Lists
async function loadList(entity) {
    const container = document.getElementById(`${entity}-list`);
    if (!container) return;
    
    container.innerHTML = '<div class="loading"><i class="bx bx-loader-alt bx-spin"></i> Carregando...</div>';
    
    try {
        const response = await fetch(`${API_BASE}/${entity}`);
        const data = await response.json();
        
        if (data.length === 0) {
            container.innerHTML = `<div class="empty"><i class='bx bx-folder-open'></i>Nenhum registro encontrado</div>`;
            return;
        }
        
        container.innerHTML = renderTable(entity, data);
        attachTableRowEvents(entity);
    } catch (error) {
        console.error('Error:', error);
        container.innerHTML = '<div class="empty"><i class="bx bx-error-circle"></i>Erro ao carregar dados</div>';
    }
}

// Dashboard
async function loadDashboard() {
    try {
        const [tutores, pets, veterinarios, consultas] = await Promise.all([
            fetch(`${API_BASE}/tutores`).then(r => r.json()),
            fetch(`${API_BASE}/pets`).then(r => r.json()),
            fetch(`${API_BASE}/veterinarios`).then(r => r.json()),
            fetch(`${API_BASE}/consultas`).then(r => r.json())
        ]);
        
        document.getElementById('total-tutores').textContent = tutores.length;
        document.getElementById('total-pets').textContent = pets.length;
        document.getElementById('total-veterinarios').textContent = veterinarios.length;
        document.getElementById('total-consultas').textContent = consultas.length;
        
        // Adicionar evento de clique nos cards (redireciona para o módulo correspondente)
        const statCards = document.querySelectorAll('.stat-card');
        if (statCards.length >= 4) {
            statCards[0].onclick = () => { switchModule('tutores'); };
            statCards[1].onclick = () => { switchModule('pets'); };
            statCards[2].onclick = () => { switchModule('veterinarios'); };
            statCards[3].onclick = () => { switchModule('consultas'); };
        }
        
        const recent = consultas.slice(-5).reverse();
        const recentHtml = `
            <table>
                <thead>
                    <tr><th><i class='bx bx-hash'></i>ID</th><th><i class='bx bx-calendar'></i>Data/Hora</th><th><i class='bx bxs-dog'></i>Pet</th><th><i class='bx bxs-stethoscope'></i>Veterinário</th><th><i class='bx bx-check-circle'></i>Status</th></tr>
                </thead>
                <tbody>
                    ${recent.map(c => `
                        <tr>
                            <td>${c.id}</td>
                            <td>${new Date(c.dataHora).toLocaleString()}</td>
                            <td>${c.pet?.nome || 'N/A'}</td>
                            <td>${c.veterinario?.nome || 'N/A'}</td>
                            <td>${c.status}</td>
                        </tr>
                    `).join('')}
                    ${recent.length === 0 ? '<tr><td colspan="5"><div class="empty"><i class="bx bx-data"></i>Nenhuma consulta encontrada</div></td></tr>' : ''}
                </tbody>
            </table>
        `;
        document.getElementById('recent-consultas').innerHTML = recentHtml;
    } catch (error) {
        console.error('Error:', error);
        document.getElementById('recent-consultas').innerHTML = '<div class="empty"><i class="bx bx-error-circle"></i>Erro ao carregar</div>';
    }
}


// Função para mostrar pets de um tutor específico
async function showPetsByTutor(tutorId, tutorNome) {
    try {
        const pets = await fetch(`${API_BASE}/pets/tutor/${tutorId}`).then(r => r.json());
        
        let petsHtml = '';
        if (pets.length === 0) {
            petsHtml = '<div class="empty"><i class="bx bx-folder-open"></i>Nenhum pet cadastrado para este tutor</div>';
        } else {
            petsHtml = `
                <div style="display: flex; flex-wrap: wrap; gap: 16px;">
                    ${pets.map(pet => `
                        <div class="pet-card" style="border: 1px solid #e2e8f0; border-radius: 12px; padding: 12px; width: 180px; text-align: center; cursor: pointer; transition: all 0.2s;">
                            <div style="width: 80px; height: 80px; border-radius: 50%; overflow: hidden; margin: 0 auto 8px; background: #f1f5f9; display: flex; align-items: center; justify-content: center;">
                                ${pet.fotoUrl ? `<img src="${pet.fotoUrl}" style="width:100%; height:100%; object-fit: cover;">` : '<i class="bx bxs-camera" style="font-size: 40px; color: #94a3b8;"></i>'}
                            </div>
                            <h4 style="margin: 8px 0 4px;">${pet.nome}</h4>
                            <p style="font-size: 12px; color: #64748b;">${pet.especie} | ${pet.raca || '-'}</p>
                            <div style="margin-top: 8px;">
                                <button class="btn-edit" style="padding: 4px 8px; font-size: 11px;" onclick="event.stopPropagation(); editItem('pets', ${pet.id}); closePetsModal();"><i class='bx bx-edit'></i></button>
                                <button class="btn-delete" style="padding: 4px 8px; font-size: 11px;" onclick="event.stopPropagation(); deleteItem('pets', ${pet.id}); closePetsModal();"><i class='bx bx-trash'></i></button>
                            </div>
                        </div>
                    `).join('')}
                </div>
            `;
        }
        
        // Remover modal existente se houver
        const existingModal = document.getElementById('temp-pets-modal');
        if (existingModal) {
            existingModal.remove();
        }
        
        // Criar o modal
        const modalDiv = document.createElement('div');
        modalDiv.id = 'temp-pets-modal';
        modalDiv.className = 'modal';
        modalDiv.style.display = 'flex';
        modalDiv.style.alignItems = 'center';
        modalDiv.style.justifyContent = 'center';
        
        modalDiv.innerHTML = `
            <div class="modal-content" style="max-width: 700px;">
                <div class="modal-header">
                    <div class="modal-title">
                        <i class='bx bxs-group'></i>
                        <h3>Pets de ${tutorNome}</h3>
                    </div>
                    <i class='bx bx-x close' onclick="closePetsModal()"></i>
                </div>
                <div style="padding: 24px;">
                    ${petsHtml}
                </div>
            </div>
        `;
        
        document.body.appendChild(modalDiv);
        modalDiv.classList.add('active');
        
    } catch (error) {
        console.error('Erro ao carregar pets do tutor:', error);
        alert('Erro ao carregar pets do tutor');
    }
}

function closePetsModal() {
    const modal = document.getElementById('temp-pets-modal');
    if (modal) {
        modal.classList.remove('active');
        setTimeout(() => {
            if (modal.parentNode) {
                modal.remove();
            }
        }, 300);
    }
}

function attachTableRowEvents(entity) {
    const rows = document.querySelectorAll(`#${entity}-list tbody tr`);
    const singularMap = {
        'tutores': 'tutor',
        'pets': 'pet',
        'veterinarios': 'veterinario',
        'consultas': 'consulta',
        'agendas': 'agenda'
    };
    const singular = singularMap[entity];
    
    rows.forEach(row => {
        const firstCell = row.cells[0];
        if (firstCell) {
            const id = parseInt(firstCell.textContent);
            let nome = '';
            if (entity === 'tutores' && row.cells[1]) {
                nome = row.cells[1].textContent;
            }
            
            row.addEventListener('click', (e) => {
                if (e.target.tagName !== 'BUTTON') {
                    if (entity === 'tutores') {
                        showPetsByTutor(id, nome);
                    } else {
                        showDetailModal(singular, id);
                    }
                }
            });
        }
    });
}

function renderTable(entity, data) {
    const headers = {
        tutores: ['ID', 'Nome', 'CPF', 'Telefone', 'Email', 'Ações'],
        pets: ['ID', 'Foto', 'Nome', 'Espécie', 'Raça', 'Tutor', 'Ações'],
        veterinarios: ['ID', 'Nome', 'CRMV', 'Especialidade', 'Telefone', 'Ações'],
        consultas: ['ID', 'Data/Hora', 'Pet', 'Veterinário', 'Status', 'Diagnóstico', 'Ações'],
        agendas: ['ID', 'Início', 'Fim', 'Veterinário', 'Disponível', 'Ações']
    };
    
    const headerIcons = {
        tutores: ['bx-hash', 'bx-user', 'bx-id-card', 'bx-phone', 'bx-envelope', 'bx-cog'],
        pets: ['bx-hash', 'bx-camera', 'bx-paw', 'bx-category', 'bx-tag', 'bx-user', 'bx-cog'],
        veterinarios: ['bx-hash', 'bx-user', 'bx-badge', 'bx-briefcase', 'bx-phone', 'bx-cog'],
        consultas: ['bx-hash', 'bx-calendar', 'bx-paw', 'bx-stethoscope', 'bx-check-circle', 'bx-file', 'bx-cog'],
        agendas: ['bx-hash', 'bx-calendar', 'bx-calendar', 'bx-stethoscope', 'bx-check', 'bx-cog']
    };
    
    return `
        <table>
            <thead>
                <tr>
                    ${headers[entity].map((h, i) => `<th><i class='bx ${headerIcons[entity][i]}'></i>${h}</th>`).join('')}
                </tr>
            </thead>
            <tbody>
                ${data.map(item => renderRow(entity, item)).join('')}
            </tbody>
        </table>
    `;
}

function renderRow(entity, item) {
    switch(entity) {
        case 'tutores':
            return `<tr>
                <td>${item.id}</td>
                <td>${item.nome}</td>
                <td>${item.cpf}</td>
                <td>${item.telefone || '-'}</td>
                <td>${item.email || '-'}</td>
                <td><button class="btn-edit" onclick="editItem('tutores', ${item.id}); event.stopPropagation();"><i class='bx bx-edit'></i>Editar</button><button class="btn-delete" onclick="deleteItem('tutores', ${item.id}); event.stopPropagation();"><i class='bx bx-trash'></i>Excluir</button></td>
            </tr>`;
        case 'pets':
            return `<tr>
                <td>${item.id}</td>
                <td style="width: 50px; text-align: center;">
                    ${item.fotoUrl ? `<img src="${item.fotoUrl}" style="width: 40px; height: 40px; border-radius: 50%; object-fit: cover;">` : '<i class="bx bx-camera" style="font-size: 24px; color: #94a3b8;"></i>'}
                </td>
                <td>${item.nome}</td>
                <td>${item.especie}</td>
                <td>${item.raca || '-'}</td>
                <td>${item.tutor?.nome || '-'}</td>
                <td><button class="btn-edit" onclick="editItem('pets', ${item.id}); event.stopPropagation();"><i class='bx bx-edit'></i>Editar</button><button class="btn-delete" onclick="deleteItem('pets', ${item.id}); event.stopPropagation();"><i class='bx bx-trash'></i>Excluir</button></td>
            </tr>`;
        case 'veterinarios':
            return `<tr>
                <td>${item.id}</td>
                <td>${item.nome}</td>
                <td>${item.crmv}</td>
                <td>${item.especialidade || '-'}</td>
                <td>${item.telefone || '-'}</td>
                <td><button class="btn-edit" onclick="editItem('veterinarios', ${item.id}); event.stopPropagation();"><i class='bx bx-edit'></i>Editar</button><button class="btn-delete" onclick="deleteItem('veterinarios', ${item.id}); event.stopPropagation();"><i class='bx bx-trash'></i>Excluir</button></td>
            </tr>`;
        case 'consultas':
            return `<tr>
                <td>${item.id}</td>
                <td>${new Date(item.dataHora).toLocaleString()}</td>
                <td>${item.pet?.nome || '-'}</td>
                <td>${item.veterinario?.nome || '-'}</td>
                <td>${item.status}</td>
                <td style="text-align: center;">
                    ${item.diagnosticoUrl ? `<a href="${item.diagnosticoUrl}" target="_blank" style="color: #3b82f6;"><i class='bx bx-file'></i> Ver</a>` : '-'}
                </td>
                <td><button class="btn-edit" onclick="editItem('consultas', ${item.id}); event.stopPropagation();"><i class='bx bx-edit'></i>Editar</button><button class="btn-delete" onclick="deleteItem('consultas', ${item.id}); event.stopPropagation();"><i class='bx bx-trash'></i>Excluir</button></td>
            </tr>`;
        case 'agendas':
            return `<tr>
                <td>${item.id}</td>
                <td>${new Date(item.dataHoraInicio).toLocaleString()}</td>
                <td>${new Date(item.dataHoraFim).toLocaleString()}</td>
                <td>${item.veterinario?.nome || '-'}</td>
                <td>${item.disponivel ? 'Sim' : 'Não'}</td>
                <td><button class="btn-edit" onclick="editItem('agendas', ${item.id}); event.stopPropagation();"><i class='bx bx-edit'></i>Editar</button><button class="btn-delete" onclick="deleteItem('agendas', ${item.id}); event.stopPropagation();"><i class='bx bx-trash'></i>Excluir</button></td>
            </tr>`;
        default:
            return '';
    }
}

// Show Modal (Create/Edit)
async function showModal(entity, id = null) {
    currentEntity = entity;
    editingId = id;
    
    const titles = {
        tutor: 'Cadastrar Tutor',
        pet: 'Cadastrar Pet',
        veterinario: 'Cadastrar Veterinário',
        consulta: 'Cadastrar Consulta',
        agenda: 'Cadastrar Agenda'
    };
    
    document.getElementById('modal-title').textContent = titles[entity] || 'Formulário';
    document.getElementById('modal-fields').innerHTML = await loadFormFields(entity, id);
    document.getElementById('modal').classList.add('active');
    
    // Adicionar evento de pré-visualização da foto do pet
    if (entity === 'pet') {
        setTimeout(() => {
            const fotoInput = document.getElementById('fotoUrl');
            if (fotoInput) {
                fotoInput.addEventListener('input', (e) => {
                    const preview = document.getElementById('preview-foto');
                    if (e.target.value) {
                        preview.innerHTML = `<img src="${e.target.value}" style="width:100%; height:100%; object-fit: cover;">`;
                    } else {
                        preview.innerHTML = '<i class="bx bx-camera" style="font-size: 48px; color: #ccc;"></i>';
                    }
                });
            }
        }, 100);
    }
}

async function loadFormFields(entity, id) {
    let data = {};
    if (id) {
        try {
            const pluralMap = { 'tutor': 'tutores', 'pet': 'pets', 'veterinario': 'veterinarios', 'consulta': 'consultas', 'agenda': 'agendas' };
            const pluralEntity = pluralMap[entity];
            const response = await fetch(`${API_BASE}/${pluralEntity}/${id}`);
            if (response.ok) data = await response.json();
        } catch (error) {
            console.error(`Erro ao carregar ${entity} para edição:`, error);
        }
    }
    
    switch(entity) {
        case 'tutor':
            return `
                <div class="form-group"><label><i class='bx bx-user'></i>Nome*</label><input type="text" id="nome" value="${data.nome || ''}" required></div>
                <div class="form-group"><label><i class='bx bx-id-card'></i>CPF*</label><input type="text" id="cpf" value="${data.cpf || ''}" required></div>
                <div class="form-group"><label><i class='bx bx-phone'></i>Telefone</label><input type="text" id="telefone" value="${data.telefone || ''}"></div>
                <div class="form-group"><label><i class='bx bx-envelope'></i>Email</label><input type="email" id="email" value="${data.email || ''}"></div>
                <div class="form-group"><label><i class='bx bx-map'></i>Endereço</label><input type="text" id="endereco" value="${data.endereco || ''}"></div>
            `;
        case 'pet':
            let tutores = [];
            try {
                const response = await fetch(`${API_BASE}/tutores`);
                if (response.ok) tutores = await response.json();
            } catch (error) { console.error('Erro ao carregar tutores:', error); }
            return `
                <div class="form-group"><label><i class='bx bx-paw'></i>Nome*</label><input type="text" id="nome" value="${data.nome || ''}" required></div>
                <div class="form-group"><label><i class='bx bx-category'></i>Espécie*</label>
                    <select id="especie">${ESPECIES.map(e => `<option ${data.especie === e ? 'selected' : ''}>${e}</option>`).join('')}</select>
                </div>
                <div class="form-group"><label><i class='bx bx-tag'></i>Raça</label><input type="text" id="raca" value="${data.raca || ''}"></div>
                <div class="form-group"><label><i class='bx bx-calendar'></i>Data Nascimento</label><input type="date" id="dataNascimento" value="${data.dataNascimento || ''}"></div>
                <div class="form-group"><label><i class='bx bx-palette'></i>Cor</label><input type="text" id="cor" value="${data.cor || ''}"></div>
                <div class="form-group"><label><i class='bx bx-weight'></i>Peso (kg)</label><input type="number" step="0.1" id="peso" value="${data.peso || ''}"></div>
                <div class="form-group"><label><i class='bx bx-user'></i>Tutor*</label>
                    <select id="tutorId" required>
                        <option value="">Selecione um tutor</option>
                        ${tutores.map(t => `<option value="${t.id}" ${data.tutor?.id === t.id ? 'selected' : ''}>${t.nome}</option>`).join('')}
                    </select>
                </div>
                <div class="form-group"><label><i class='bx bx-image'></i>URL da Foto do Pet</label>
                    <input type="text" id="fotoUrl" value="${data.fotoUrl || ''}" placeholder="https://exemplo.com/foto.jpg">
                </div>
                <div class="form-group">
                    <label>Pré-visualização</label>
                    <div id="preview-foto" style="width: 100px; height: 100px; border-radius: 50%; overflow: hidden; background: #f1f5f9; display: flex; align-items: center; justify-content: center;">
                        ${data.fotoUrl ? `<img src="${data.fotoUrl}" style="width:100%; height:100%; object-fit: cover;">` : '<i class="bx bx-camera" style="font-size: 48px; color: #ccc;"></i>'}
                    </div>
                </div>
            `;
        case 'veterinario':
            return `
                <div class="form-group"><label><i class='bx bx-user'></i>Nome*</label><input type="text" id="nome" value="${data.nome || ''}" required></div>
                <div class="form-group"><label><i class='bx bx-badge'></i>CRMV*</label><input type="text" id="crmv" value="${data.crmv || ''}" required></div>
                <div class="form-group"><label><i class='bx bx-briefcase'></i>Especialidade</label><input type="text" id="especialidade" value="${data.especialidade || ''}"></div>
                <div class="form-group"><label><i class='bx bx-phone'></i>Telefone</label><input type="text" id="telefone" value="${data.telefone || ''}"></div>
                <div class="form-group"><label><i class='bx bx-envelope'></i>Email</label><input type="email" id="email" value="${data.email || ''}"></div>
            `;
        case 'consulta':
            let pets = [], veterinarios = [];
            let consultaData = data;
            try {
                const [petsRes, vetsRes] = await Promise.all([fetch(`${API_BASE}/pets`), fetch(`${API_BASE}/veterinarios`)]);
                if (petsRes.ok) pets = await petsRes.json();
                if (vetsRes.ok) veterinarios = await vetsRes.json();
            } catch (error) { console.error('Erro ao carregar dados para consulta:', error); }
            return `
                <div class="form-group"><label><i class='bx bx-calendar'></i>Data/Hora*</label><input type="datetime-local" id="dataHora" value="${consultaData.dataHora?.slice(0,16) || ''}" required></div>
                <div class="form-group"><label><i class='bx bxs-dog'></i>Pet*</label>
                    <select id="petId" required>
                        <option value="">Selecione um pet</option>
                        ${pets.map(p => `<option value="${p.id}" ${consultaData.pet?.id === p.id ? 'selected' : ''}>${p.nome}</option>`).join('')}
                    </select>
                </div>
                <div class="form-group"><label><i class='bx bxs-stethoscope'></i>Veterinário*</label>
                    <select id="veterinarioId" required>
                        <option value="">Selecione um veterinário</option>
                        ${veterinarios.map(v => `<option value="${v.id}" ${consultaData.veterinario?.id === v.id ? 'selected' : ''}>${v.nome}</option>`).join('')}
                    </select>
                </div>
                <div class="form-group"><label><i class='bx bx-check-circle'></i>Status*</label>
                    <select id="status">${STATUS_CONSULTA.map(s => `<option ${consultaData.status === s ? 'selected' : ''}>${s}</option>`).join('')}</select>
                </div>
                <div class="form-group"><label><i class='bx bx-note'></i>Observações</label><textarea id="observacoes" rows="3">${consultaData.observacoes || ''}</textarea></div>
                <div class="form-group"><label><i class='bx bx-stethoscope'></i>Diagnóstico (texto)</label><textarea id="diagnostico" rows="2">${consultaData.diagnostico || ''}</textarea></div>
                <div class="form-group"><label><i class='bx bx-file'></i>Diagnóstico (arquivo)</label>
                    <input type="file" id="diagnosticoFile" accept=".pdf,.jpg,.png,.jpeg">
                    ${consultaData.diagnosticoUrl ? `<div style="margin-top: 8px;"><a href="${consultaData.diagnosticoUrl}" target="_blank" style="color: #3b82f6;"><i class='bx bx-file'></i> Ver arquivo atual</a></div>` : ''}
                </div>
                <div class="form-group"><label><i class='bx bx-capsule'></i>Prescrição</label><textarea id="prescricao" rows="2">${consultaData.prescricao || ''}</textarea></div>
            `;
        case 'agenda':
            let veterinariosAgenda = [];
            try {
                const response = await fetch(`${API_BASE}/veterinarios`);
                if (response.ok) veterinariosAgenda = await response.json();
            } catch (error) { console.error('Erro ao carregar veterinários para agenda:', error); }
            return `
                <div class="form-group"><label><i class='bx bx-calendar'></i>Início*</label><input type="datetime-local" id="dataHoraInicio" value="${data.dataHoraInicio?.slice(0,16) || ''}" required></div>
                <div class="form-group"><label><i class='bx bx-calendar'></i>Fim*</label><input type="datetime-local" id="dataHoraFim" value="${data.dataHoraFim?.slice(0,16) || ''}" required></div>
                <div class="form-group"><label><i class='bx bxs-stethoscope'></i>Veterinário*</label>
                    <select id="veterinarioId" required>
                        <option value="">Selecione um veterinário</option>
                        ${veterinariosAgenda.map(v => `<option value="${v.id}" ${data.veterinario?.id === v.id ? 'selected' : ''}>${v.nome}</option>`).join('')}
                    </select>
                </div>
                <div class="form-group"><label><i class='bx bx-check'></i>Disponível</label>
                    <select id="disponivel">
                        <option value="true">Sim</option>
                        <option value="false" ${data.disponivel === false ? 'selected' : ''}>Não</option>
                    </select>
                </div>
            `;
        default:
            return '<p class="empty">Formulário não disponível para esta entidade</p>';
    }
}

// Save
document.getElementById('modal-form').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const entity = currentEntity;
    const isEdit = editingId !== null;
    
    const pluralMap = { 'tutor': 'tutores', 'pet': 'pets', 'veterinario': 'veterinarios', 'consulta': 'consultas', 'agenda': 'agendas' };
    const urlEntity = pluralMap[entity];
    const url = isEdit ? `${API_BASE}/${urlEntity}/${editingId}` : `${API_BASE}/${urlEntity}`;
    const method = isEdit ? 'PUT' : 'POST';
    
    let body = {};
    let savedId = editingId;
    
    switch(entity) {
        case 'tutor':
            body = { nome: document.getElementById('nome').value, cpf: document.getElementById('cpf').value, telefone: document.getElementById('telefone').value, email: document.getElementById('email').value, endereco: document.getElementById('endereco').value };
            break;
        case 'pet':
            body = { 
                nome: document.getElementById('nome').value, 
                especie: document.getElementById('especie').value, 
                raca: document.getElementById('raca').value, 
                dataNascimento: document.getElementById('dataNascimento').value, 
                cor: document.getElementById('cor').value, 
                peso: parseFloat(document.getElementById('peso').value) || null, 
                tutor: { id: parseInt(document.getElementById('tutorId').value) },
                fotoUrl: document.getElementById('fotoUrl').value
            };
            break;
        case 'veterinario':
            body = { nome: document.getElementById('nome').value, crmv: document.getElementById('crmv').value, especialidade: document.getElementById('especialidade').value, telefone: document.getElementById('telefone').value, email: document.getElementById('email').value };
            break;
        case 'consulta':
            body = { 
                dataHora: document.getElementById('dataHora').value, 
                pet: { id: parseInt(document.getElementById('petId').value) }, 
                veterinario: { id: parseInt(document.getElementById('veterinarioId').value) }, 
                status: document.getElementById('status').value, 
                observacoes: document.getElementById('observacoes').value, 
                diagnostico: document.getElementById('diagnostico').value, 
                prescricao: document.getElementById('prescricao').value 
            };
            break;
        case 'agenda':
            body = { dataHoraInicio: document.getElementById('dataHoraInicio').value, dataHoraFim: document.getElementById('dataHoraFim').value, veterinario: { id: parseInt(document.getElementById('veterinarioId').value) }, disponivel: document.getElementById('disponivel').value === 'true' };
            break;
    }
    
    try {
        const response = await fetch(url, { method: method, headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(body) });
        if (response.ok) {
            const savedData = await response.json();
            savedId = savedData.id;
            
            // Upload do arquivo de diagnóstico (se houver)
            if (entity === 'consulta') {
                const fileInput = document.getElementById('diagnosticoFile');
                if (fileInput && fileInput.files.length > 0) {
                    const formData = new FormData();
                    formData.append('file', fileInput.files[0]);
                    await fetch(`${API_BASE}/consultas/${savedId}/upload-diagnostico`, { method: 'POST', body: formData });
                }
            }
            
            closeModal();
            loadList(currentModule);
            if (currentModule === 'dashboard') loadDashboard();
        } else { alert('Erro ao salvar: ' + await response.text()); }
    } catch (error) { alert('Erro de conexão com o servidor.'); }
});

function editItem(entity, id) {
    const singularMap = { 'tutores': 'tutor', 'pets': 'pet', 'veterinarios': 'veterinario', 'consultas': 'consulta', 'agendas': 'agenda' };
    showModal(singularMap[entity], id);
}

async function deleteItem(entity, id) {
    if (confirm('Tem certeza que deseja excluir este registro?')) {
        try {
            const response = await fetch(`${API_BASE}/${entity}/${id}`, { method: 'DELETE' });
            if (response.ok) { 
                loadList(currentModule);
                if (currentModule === 'dashboard') loadDashboard();
            }
            else { alert('Erro ao excluir'); }
        } catch (error) { alert('Erro de conexão com o servidor'); }
    }
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
    document.getElementById('modal-form').reset();
    editingId = null;
    currentEntity = null;
}

// Detail Modal
async function showDetailModal(entityType, entityId) {
    const pluralMap = { 'tutor': 'tutores', 'pet': 'pets', 'veterinario': 'veterinarios', 'consulta': 'consultas', 'agenda': 'agendas' };
    const pluralEntity = pluralMap[entityType];
    const titleMap = { 'tutor': 'Detalhes do Tutor', 'pet': 'Detalhes do Pet', 'veterinario': 'Detalhes do Veterinário', 'consulta': 'Detalhes da Consulta', 'agenda': 'Detalhes da Agenda' };
    
    document.getElementById('detail-modal-title').textContent = titleMap[entityType];
    document.getElementById('detail-modal-body').innerHTML = '<div class="loading"><i class="bx bx-loader-alt bx-spin"></i> Carregando...</div>';
    document.getElementById('detail-modal').classList.add('active');
    
    try {
        const response = await fetch(`${API_BASE}/${pluralEntity}/${entityId}`);
        if (response.ok) { renderDetailModal(entityType, await response.json()); }
        else { document.getElementById('detail-modal-body').innerHTML = '<div class="empty"><i class="bx bx-error-circle"></i>Erro ao carregar dados</div>'; }
    } catch (error) { document.getElementById('detail-modal-body').innerHTML = '<div class="empty"><i class="bx bx-error-circle"></i>Erro de conexão</div>'; }
}

function renderDetailModal(entityType, data) {
    let html = '';
    switch(entityType) {
        case 'tutor':
            html = `<div class="detail-card"><h4><i class='bx bx-user'></i> Informações Pessoais</h4>
                <div class="detail-row"><span class="detail-label">ID:</span><span class="detail-value">${data.id}</span></div>
                <div class="detail-row"><span class="detail-label">Nome:</span><span class="detail-value">${data.nome}</span></div>
                <div class="detail-row"><span class="detail-label">CPF:</span><span class="detail-value">${data.cpf}</span></div>
                <div class="detail-row"><span class="detail-label">Telefone:</span><span class="detail-value">${data.telefone || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Email:</span><span class="detail-value">${data.email || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Endereço:</span><span class="detail-value">${data.endereco || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Cadastro:</span><span class="detail-value">${new Date(data.dataCadastro).toLocaleDateString()}</span></div>
            </div>`;
            break;
        case 'pet':
            html = `<div class="detail-card"><h4><i class='bx bxs-dog'></i> Informações do Pet</h4>
                <div class="detail-row"><span class="detail-label">ID:</span><span class="detail-value">${data.id}</span></div>
                <div class="detail-row"><span class="detail-label">Nome:</span><span class="detail-value">${data.nome}</span></div>
                <div class="detail-row"><span class="detail-label">Espécie:</span><span class="detail-value">${data.especie}</span></div>
                <div class="detail-row"><span class="detail-label">Raça:</span><span class="detail-value">${data.raca || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Cor:</span><span class="detail-value">${data.cor || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Peso:</span><span class="detail-value">${data.peso ? data.peso + ' kg' : '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Nascimento:</span><span class="detail-value">${data.dataNascimento ? new Date(data.dataNascimento).toLocaleDateString() : '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Tutor:</span><span class="detail-value">${data.tutor?.nome || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Foto:</span><span class="detail-value">${data.fotoUrl ? `<a href="${data.fotoUrl}" target="_blank">Ver imagem</a>` : '-'}</span></div>
            </div>`;
            break;
        case 'veterinario':
            html = `<div class="detail-card"><h4><i class='bx bxs-stethoscope'></i> Informações do Veterinário</h4>
                <div class="detail-row"><span class="detail-label">ID:</span><span class="detail-value">${data.id}</span></div>
                <div class="detail-row"><span class="detail-label">Nome:</span><span class="detail-value">${data.nome}</span></div>
                <div class="detail-row"><span class="detail-label">CRMV:</span><span class="detail-value">${data.crmv}</span></div>
                <div class="detail-row"><span class="detail-label">Especialidade:</span><span class="detail-value">${data.especialidade || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Telefone:</span><span class="detail-value">${data.telefone || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Email:</span><span class="detail-value">${data.email || '-'}</span></div>
            </div>`;
            break;
        case 'consulta':
            html = `<div class="detail-card"><h4><i class='bx bxs-calendar-check'></i> Informações da Consulta</h4>
                <div class="detail-row"><span class="detail-label">ID:</span><span class="detail-value">${data.id}</span></div>
                <div class="detail-row"><span class="detail-label">Data/Hora:</span><span class="detail-value">${new Date(data.dataHora).toLocaleString()}</span></div>
                <div class="detail-row"><span class="detail-label">Status:</span><span class="detail-value">${data.status}</span></div>
                <div class="detail-row"><span class="detail-label">Pet:</span><span class="detail-value">${data.pet?.nome || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Veterinário:</span><span class="detail-value">${data.veterinario?.nome || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Observações:</span><span class="detail-value">${data.observacoes || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Diagnóstico:</span><span class="detail-value">${data.diagnostico || '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Arquivo Diagnóstico:</span><span class="detail-value">${data.diagnosticoUrl ? `<a href="${data.diagnosticoUrl}" target="_blank">Download</a>` : '-'}</span></div>
                <div class="detail-row"><span class="detail-label">Prescrição:</span><span class="detail-value">${data.prescricao || '-'}</span></div>
            </div>`;
            break;
        case 'agenda':
            html = `<div class="detail-card"><h4><i class='bx bxs-calendar-week'></i> Informações da Agenda</h4>
                <div class="detail-row"><span class="detail-label">ID:</span><span class="detail-value">${data.id}</span></div>
                <div class="detail-row"><span class="detail-label">Início:</span><span class="detail-value">${new Date(data.dataHoraInicio).toLocaleString()}</span></div>
                <div class="detail-row"><span class="detail-label">Fim:</span><span class="detail-value">${new Date(data.dataHoraFim).toLocaleString()}</span></div>
                <div class="detail-row"><span class="detail-label">Disponível:</span><span class="detail-value">${data.disponivel ? 'Sim' : 'Não'}</span></div>
                <div class="detail-row"><span class="detail-label">Veterinário:</span><span class="detail-value">${data.veterinario?.nome || '-'}</span></div>
            </div>`;
            break;
        default: html = '<div class="empty">Visualização não disponível</div>';
    }
    document.getElementById('detail-modal-body').innerHTML = html;
}

function closeDetailModal() {
    document.getElementById('detail-modal').classList.remove('active');
}