// API Base URL
const API_BASE_URL = 'http://localhost:8181/rest';

// Tab switching
function showTab(tabName) {
    // Hide all tabs
    const tabs = document.querySelectorAll('.tab-content');
    tabs.forEach(tab => tab.classList.remove('active'));

    // Remove active class from all buttons
    const buttons = document.querySelectorAll('.tab-button');
    buttons.forEach(button => button.classList.remove('active'));

    // Show selected tab
    document.getElementById(tabName).classList.add('active');

    // Add active class to clicked button
    event.target.classList.add('active');
}

// Show message helper
function showMessage(elementId, message, type = 'info') {
    const element = document.getElementById(elementId);
    element.textContent = message;
    element.className = `result-message ${type}`;
    element.style.display = 'block';

    // Auto hide after 5 seconds
    setTimeout(() => {
        element.style.display = 'none';
    }, 5000);
}

// Absensi Form
document.getElementById('absensiForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const key = document.getElementById('keyAbsensi').value;

    try {
        const response = await fetch(`${API_BASE_URL}/absensi`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ key: key })
        });

        const data = await response.json();
        
        if (response.ok) {
            showMessage('absensiResult', `Sukses! ${data.keterangan || 'Absensi berhasil'}`, 'success');
            document.getElementById('keyAbsensi').value = '';
        } else {
            showMessage('absensiResult', `Error: ${data.keterangan || 'Terjadi kesalahan'}`, 'error');
        }
    } catch (error) {
        showMessage('absensiResult', `Error: ${error.message}. Pastikan backend berjalan di ${API_BASE_URL}`, 'error');
    }
});

// Cek Absensi Batch Form
document.getElementById('cekAbsensiForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    const keysInput = document.getElementById('keysAbsensi').value;
    const keys = keysInput.split(',').map(key => key.trim()).filter(key => key);

    try {
        const response = await fetch(`${API_BASE_URL}/cekabsensibatch`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(keys)
        });

        const data = await response.json();
        
        if (response.ok) {
            let resultHTML = '<h3>Hasil Pengecekan:</h3><table><thead><tr><th>Key</th><th>Nama</th><th>Status</th></tr></thead><tbody>';
            data.forEach(item => {
                resultHTML += `<tr><td>${item.key || 'N/A'}</td><td>${item.nama || 'N/A'}</td><td>${item.status || 'N/A'}</td></tr>`;
            });
            resultHTML += '</tbody></table>';
            
            const resultDiv = document.getElementById('cekAbsensiResult');
            resultDiv.innerHTML = resultHTML;
            resultDiv.className = 'result-message info';
            resultDiv.style.display = 'block';
        } else {
            showMessage('cekAbsensiResult', 'Error: Gagal mengecek absensi', 'error');
        }
    } catch (error) {
        showMessage('cekAbsensiResult', `Error: ${error.message}. Pastikan backend berjalan di ${API_BASE_URL}`, 'error');
    }
});

// Tambah Anggota Form
document.getElementById('tambahAnggotaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const anggotaData = {
        key: document.getElementById('keyAnggota').value,
        nama: document.getElementById('namaAnggota').value,
        kelas: document.getElementById('kelasAnggota').value,
        idDivisi: parseInt(document.getElementById('divisiAnggota').value)
    };

    try {
        const response = await fetch(`${API_BASE_URL}/tambahanggota`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(anggotaData)
        });

        const data = await response.json();
        
        if (response.ok) {
            showMessage('tambahAnggotaResult', `Sukses! ${data.keterangan || 'Anggota berhasil ditambahkan'}`, 'success');
            document.getElementById('tambahAnggotaForm').reset();
        } else {
            showMessage('tambahAnggotaResult', `Error: ${data.keterangan || 'Terjadi kesalahan'}`, 'error');
        }
    } catch (error) {
        showMessage('tambahAnggotaResult', `Error: ${error.message}. Pastikan backend berjalan di ${API_BASE_URL}`, 'error');
    }
});

// Update Anggota Form
document.getElementById('updateAnggotaForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const anggotaData = {
        key: document.getElementById('keyAnggotaUpdate').value,
        nama: document.getElementById('namaAnggotaUpdate').value,
        kelas: document.getElementById('kelasAnggotaUpdate').value,
        idDivisi: parseInt(document.getElementById('divisiAnggotaUpdate').value)
    };

    try {
        const response = await fetch(`${API_BASE_URL}/updateanggota`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(anggotaData)
        });

        const data = await response.json();
        
        if (response.ok) {
            showMessage('updateAnggotaResult', `Sukses! ${data.keterangan || 'Anggota berhasil diupdate'}`, 'success');
            document.getElementById('updateAnggotaForm').reset();
        } else {
            showMessage('updateAnggotaResult', `Error: ${data.keterangan || 'Terjadi kesalahan'}`, 'error');
        }
    } catch (error) {
        showMessage('updateAnggotaResult', `Error: ${error.message}. Pastikan backend berjalan di ${API_BASE_URL}`, 'error');
    }
});

// Tambah Batch Form
document.getElementById('tambahBatchForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    try {
        const jsonData = JSON.parse(document.getElementById('jsonBatch').value);
        
        const response = await fetch(`${API_BASE_URL}/tambahanggotabatch`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(jsonData)
        });

        const data = await response.json();
        
        if (response.ok) {
            showMessage('tambahBatchResult', `Sukses! ${data.keterangan || 'Semua anggota berhasil ditambahkan'}`, 'success');
            document.getElementById('jsonBatch').value = '';
        } else {
            showMessage('tambahBatchResult', `Error: ${data.keterangan || 'Terjadi kesalahan'}`, 'error');
        }
    } catch (error) {
        if (error instanceof SyntaxError) {
            showMessage('tambahBatchResult', 'Error: Format JSON tidak valid', 'error');
        } else {
            showMessage('tambahBatchResult', `Error: ${error.message}. Pastikan backend berjalan di ${API_BASE_URL}`, 'error');
        }
    }
});

// Load Riwayat
async function loadRiwayat() {
    try {
        const response = await fetch(`${API_BASE_URL}/`);
        const data = await response.json();
        
        if (response.ok && Array.isArray(data)) {
            let tableHTML = '<table><thead><tr><th>ID</th><th>Key</th><th>Nama</th><th>Waktu Input</th><th>Keterangan</th></tr></thead><tbody>';
            
            data.forEach(item => {
                tableHTML += `<tr>
                    <td>${item.id || 'N/A'}</td>
                    <td>${item.key || 'N/A'}</td>
                    <td>${item.nama || 'N/A'}</td>
                    <td>${item.waktu_input || 'N/A'}</td>
                    <td>${item.keterangan || '-'}</td>
                </tr>`;
            });
            
            tableHTML += '</tbody></table>';
            
            const resultDiv = document.getElementById('riwayatTable');
            resultDiv.innerHTML = tableHTML;
        } else {
            document.getElementById('riwayatTable').innerHTML = '<p class="result-message info">Tidak ada data riwayat</p>';
        }
    } catch (error) {
        document.getElementById('riwayatTable').innerHTML = `<p class="result-message error">Error: ${error.message}. Pastikan backend berjalan di ${API_BASE_URL}</p>`;
    }
}

// Hapus Data Form
document.getElementById('hapusDataForm').addEventListener('submit', async (e) => {
    e.preventDefault();
    
    const id = parseInt(document.getElementById('idHapus').value);

    try {
        const response = await fetch(`${API_BASE_URL}/deletedata`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ id: id })
        });

        const data = await response.json();
        
        if (response.ok) {
            showMessage('hapusDataResult', `Sukses! ${data.keterangan || 'Data berhasil dihapus'}`, 'success');
            document.getElementById('idHapus').value = '';
            // Reload riwayat jika sedang ditampilkan
            if (document.getElementById('riwayatTable').innerHTML) {
                loadRiwayat();
            }
        } else {
            showMessage('hapusDataResult', `Error: ${data.keterangan || 'Terjadi kesalahan'}`, 'error');
        }
    } catch (error) {
        showMessage('hapusDataResult', `Error: ${error.message}. Pastikan backend berjalan di ${API_BASE_URL}`, 'error');
    }
});
