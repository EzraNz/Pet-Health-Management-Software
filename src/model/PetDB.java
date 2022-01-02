package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PetDB {
    private static void buildPetTable() throws SQLException, ClassNotFoundException {
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = """
                CREATE TABLE IF NOT EXISTS Pets (petNumber INTEGER NOT NULL PRIMARY KEY, ownerEmail TEXT NOT NULL,
                name TEXT NOT NULL, coatColor TEXT NOT NULL, dateOfBirth TEXT NOT NULL,
                species TEXT NOT NULL, notes TEXT NOT NULL)
                """;
        psqlDB.updateDB(sql);
        psqlDB.close();
    }

    private static void buildPetMedicalTable() throws SQLException, ClassNotFoundException {
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = """
                CREATE TABLE IF NOT EXISTS Pet_Medical_Information (petNumber INTEGER NOT NULL PRIMARY KEY, ownerEmail TEXT NOT NULL,
                petName TEXT NOT NULL, petWeight TEXT NOT NULL, medication TEXT NOT NULL)
                """;
        psqlDB.updateDB(sql);
        psqlDB.close();
    }

    public void addPet(Pet pet) throws Exception {
        buildPetTable();

        int petNumber = 0;
        var numberOfEntries = 0;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "SELECT COUNT(*) from pets";
        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            numberOfEntries = rs.getInt(1);
            petNumber = numberOfEntries == 0 ? 1 : numberOfEntries + 1;
        }

        psqlDB = new PsqlDB("localhost:5432/records");
        sql = "INSERT INTO Pets (ownerEmail, name, coatColor, dateOfBirth, species, notes, petNumber) Values('" + pet.getOwnerEmail() + "', '" +
                pet.getName() + "', '" + pet.getCoatColor() + "', '" + pet.getDateOfBirth() + "', '" + pet.getSpecies() +
                "', '" + pet.getNotes() + "', " + petNumber + ")";
        psqlDB.updateDB(sql);
        psqlDB.close();
    }

    public void addMedicalInformation(Pet pet) throws Exception {
        buildPetMedicalTable();

        var petNumber = 0;
        var numberOfEntries = 0;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "SELECT COUNT(*) from pet_medical_information";
        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            numberOfEntries = rs.getInt(1);
            petNumber = numberOfEntries == 0 ? 1 : numberOfEntries + 1;
        }

        psqlDB = new PsqlDB("localhost:5432/records");
        sql = "INSERT INTO Pet_Medical_Information (ownerEmail, petName, petWeight, medication, petNumber) Values('" + pet.getOwnerEmail() + "', '" +
                pet.getName() + "', '" + pet.getWeight() + "', '" + pet.getMedication() + "', " + petNumber + ")";
        psqlDB.updateDB(sql);
        psqlDB.close();
    }

    public ArrayList<Pet> medicalHistory(String petName, String ownerEmail) throws Exception {
        ArrayList<Pet> userPets = new ArrayList<>();
        if (verifyPet(petName, ownerEmail)) {
            PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
            String sql = "SELECT * from pet_medical_information WHERE ownerEmail = '" + ownerEmail + "' AND petName = '" + petName + "'";
            ResultSet rs = psqlDB.queryDB(sql);
            while (rs.next()) {
                String medication = rs.getString("medication");
                String weight = rs.getString("petWeight");
                String name = rs.getString("petName");

                Pet pet = new Pet(name, weight, medication);
                userPets.add(pet);
            }
            psqlDB.close();
        } else {
            throw new Exception("You have no pet with that name");
        }
        return userPets;
    }

    public boolean verifyPet(String petName, String ownerEmail) throws Exception {
        String tempName;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "SELECT name from pets WHERE ownerEmail = '" + ownerEmail + "'";
        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            tempName = rs.getString(1);
            if (petName.equalsIgnoreCase(tempName)) {
                return true;
            }
        }
        psqlDB.close();
        return false;
    }

    public void updatePet(Pet pet, String originalName, String ownerEmail) throws SQLException, ClassNotFoundException {
        int petNumber = 0;
        PsqlDB psqlDB = new PsqlDB("localhost:5432/records");
        String sql = "SELECT petNumber from pets WHERE ownerEmail = '" + ownerEmail + "' AND name = '" + originalName + "'";
        ResultSet rs = psqlDB.queryDB(sql);
        while (rs.next()) {
            petNumber = rs.getInt(1);
        }

        sql = "UPDATE pets SET ownerEmail = '" + ownerEmail + "', name = '" + pet.getName() + "', coatColor = '" +
                pet.getCoatColor() + "', dateOfBirth = '" + pet.getDateOfBirth() + "', species = '" + pet.getSpecies() +
                "', notes = '" + pet.getNotes() + "' WHERE petNumber = " + petNumber;
        psqlDB.updateDB(sql);
        psqlDB.close();
    }
}
