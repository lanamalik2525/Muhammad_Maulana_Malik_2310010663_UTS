package view;

import model.Barang;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * FormInventaris: Aplikasi Inventaris Barang (Simple Swing GUI)
 * Designed to be easy to read and run in NetBeans 25 or similar.
 */
public class FormInventaris extends JFrame {

    private ArrayList<Barang> daftarBarang = new ArrayList<>();

    // Swing components
    private JTextField txtKode, txtNama, txtJumlah;
    private JButton btnTambah, btnEdit, btnHapus, btnSimpan, btnLoad;
    private JTable tabelBarang;
    private DefaultTableModel tableModel;

    public FormInventaris() {
        initComponents();
        setTitle("Aplikasi Inventaris Barang - Muhammad_Maulana_Malik (2310010663)");
        setSize(700, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void initComponents() {
        // Top panel (title)
        JLabel lblTitle = new JLabel("Aplikasi Inventaris Barang", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));

        // Input panel
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6,6,6,6);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        inputPanel.add(new JLabel("Kode:"), c);
        c.gridx = 1;
        txtKode = new JTextField(12);
        inputPanel.add(txtKode, c);

        c.gridx = 0; c.gridy = 1;
        inputPanel.add(new JLabel("Nama:"), c);
        c.gridx = 1;
        txtNama = new JTextField(20);
        inputPanel.add(txtNama, c);

        c.gridx = 0; c.gridy = 2;
        inputPanel.add(new JLabel("Jumlah:"), c);
        c.gridx = 1;
        txtJumlah = new JTextField(8);
        inputPanel.add(txtJumlah, c);

        // Buttons panel
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnTambah = new JButton("Tambah");
        btnEdit = new JButton("Edit");
        btnHapus = new JButton("Hapus");
        btnSimpan = new JButton("Simpan ke File");
        btnLoad = new JButton("Muat dari File");
        btnPanel.add(btnTambah);
        btnPanel.add(btnEdit);
        btnPanel.add(btnHapus);
        btnPanel.add(btnSimpan);
        btnPanel.add(btnLoad);

        // Table
        String[] columns = {"Kode", "Nama", "Jumlah"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // non-editable
        };
        tabelBarang = new JTable(tableModel);
        JScrollPane tableScroll = new JScrollPane(tabelBarang);

        // Layout the main frame
        setLayout(new BorderLayout());
        add(lblTitle, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout());
        center.add(inputPanel, BorderLayout.NORTH);
        center.add(btnPanel, BorderLayout.CENTER);
        add(center, BorderLayout.WEST);

        add(tableScroll, BorderLayout.CENTER);

        // Event handlers
        btnTambah.addActionListener(e -> aksiTambah());
        btnEdit.addActionListener(e -> aksiEdit());
        btnHapus.addActionListener(e -> aksiHapus());
        btnSimpan.addActionListener(e -> aksiSimpanFile());
        btnLoad.addActionListener(e -> aksiMuatFile());

        // When table row clicked, populate input fields
        tabelBarang.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int idx = tabelBarang.getSelectedRow();
                if (idx >= 0) {
                    txtKode.setText(tableModel.getValueAt(idx, 0).toString());
                    txtNama.setText(tableModel.getValueAt(idx, 1).toString());
                    txtJumlah.setText(tableModel.getValueAt(idx, 2).toString());
                }
            }
        });
    }

    // Tambah data
    private void aksiTambah() {
        try {
            String kode = txtKode.getText().trim();
            String nama = txtNama.getText().trim();
            int jumlah = Integer.parseInt(txtJumlah.getText().trim());

            if (kode.isEmpty() || nama.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Kode dan Nama tidak boleh kosong", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Barang b = new Barang(kode, nama, jumlah);
            daftarBarang.add(b);
            refreshTabel();
            clearInputs();
            JOptionPane.showMessageDialog(this, "Data berhasil ditambahkan");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edit data yang dipilih
    private void aksiEdit() {
        int idx = tabelBarang.getSelectedRow();
        if (idx >= 0) {
            try {
                Barang b = daftarBarang.get(idx);
                b.setKode(txtKode.getText().trim());
                b.setNama(txtNama.getText().trim());
                b.setJumlah(Integer.parseInt(txtJumlah.getText().trim()));
                refreshTabel();
                clearInputs();
                JOptionPane.showMessageDialog(this, "Data berhasil diubah");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Jumlah harus berupa angka", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin diedit", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Hapus data
    private void aksiHapus() {
        int idx = tabelBarang.getSelectedRow();
        if (idx >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                daftarBarang.remove(idx);
                refreshTabel();
                clearInputs();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Simpan ke file data_barang.txt
    private void aksiSimpanFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("data_barang.txt"))) {
            for (Barang b : daftarBarang) {
                bw.write(b.getKode() + "," + b.getNama() + "," + b.getJumlah());
                bw.newLine();
            }
            JOptionPane.showMessageDialog(this, "Data berhasil disimpan ke data_barang.txt");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal menyimpan file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Muat dari file data_barang.txt
    private void aksiMuatFile() {
        File f = new File("data_barang.txt");
        if (!f.exists()) {
            JOptionPane.showMessageDialog(this, "File data_barang.txt tidak ditemukan", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            daftarBarang.clear();
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String kode = parts[0];
                    String nama = parts[1];
                    int jumlah = Integer.parseInt(parts[2]);
                    daftarBarang.add(new Barang(kode, nama, jumlah));
                }
            }
            refreshTabel();
            JOptionPane.showMessageDialog(this, "Data berhasil dimuat dari file");
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal membaca file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "File rusak atau format tidak sesuai", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void refreshTabel() {
        tableModel.setRowCount(0);
        for (Barang b : daftarBarang) {
            tableModel.addRow(new Object[]{b.getKode(), b.getNama(), b.getJumlah()});
        }
    }

    private void clearInputs() {
        txtKode.setText(""); txtNama.setText(""); txtJumlah.setText(""); 
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FormInventaris().setVisible(true);
        });
    }
}