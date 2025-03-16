-- Clear existing test data (to prevent duplicate entries in case of multiple test runs)
DELETE FROM drug_application_product_numbers;
DELETE FROM drug_applications;

-- Insert test data into the drug_application table
INSERT INTO drug_applications (application_number, manufacturer_name, substance_name)
VALUES ('ANDA040813', 'Renew Pharmaceuticals', 'INDOCYANINE GREEN');

-- Insert related product numbers
INSERT INTO drug_application_product_numbers (drug_application_application_number, product_numbers)
VALUES ('ANDA040813', '73624-424'), ('ANDA040813', '70100-424');