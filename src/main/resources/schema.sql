CREATE TABLE IF NOT EXISTS esbi_data (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL,
    employee_income DECIMAL(12,2) NOT NULL DEFAULT 0,
    self_employed_income DECIMAL(12,2) NOT NULL DEFAULT 0,
    business_owner_income DECIMAL(12,2) NOT NULL DEFAULT 0,
    investor_income DECIMAL(12,2) NOT NULL DEFAULT 0,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
