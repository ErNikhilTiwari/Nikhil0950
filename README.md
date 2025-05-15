# Mutual Fund SIP Tracker


## Introduction

The **Mutual Fund SIP Tracker** is a comprehensive backend application designed to empower users in managing their Systematic Investment Plans (SIPs) for mutual funds. This project aims to simplify the tracking of investments by integrating real-time NAV (Net Asset Value) data from various external sources, allowing users to analyze their financial performance effectively.


### Key Objectives

- **Investment Management**: Enable users to easily add, edit, and delete their SIPs while keeping track of important details such as fund name, investment amount, frequency, and start date.

- **NAV Tracking**: Automatically fetch and store daily NAV data from reliable APIs, ensuring users have up-to-date information on their investments.

- **Performance Analysis**: Provide robust financial calculations, including XIRR and CAGR, to help users understand their investment growth and make informed decisions.

- **Alerts and Notifications**: Implement alert systems to notify users of significant changes in NAV or underperforming funds, enhancing their ability to manage risks.

- **Dynamic Reporting**: Generate insightful reports that summarize investment performance, with options to export data in various formats for further analysis.


## Learning Focus

- **Scheduled Jobs**: Fetching NAV data daily.
- **REST API Integration**: Using APIs from AMFI.
- **Financial Calculations**: Implementing XIRR, CAGR.
- **Relational Database Design**: Structuring the database for efficient data management.
- **Dynamic and Scheduled Reporting**: Generating reports based on user data.


## Core Features

- **User Management**:
  - Registration/Login with JWT security.
  - Role-based access control.

- **SIP Plan Management**:
  - Add/Edit/Delete SIPs with:
    - Fund Name
    - Amount
    - Frequency (Weekly/Monthly)
    - Start Date

- **NAV Integration**:
  - Fetch NAVs daily from 3rd-party APIs (like AMFI XML or JSON).
  - Store historical NAVs.
  - Scheduled cron job to fetch NAVs (e.g., 9:30 AM daily).

- **Performance Tracking**:
  - Calculate total invested and current value.
  - Calculate XIRR and CAGR.
  - Alert triggers for NAV drops or poor-performing funds.

- **Reporting & Export**:
  - SIP summary report (investment vs. current value).
  - Export report in Excel & Sending to Email.


## Installation

To set up the project locally, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/dgarg7599/sip-tracker.git
   ```

2. Navigate to the project directory:
   ```bash
   cd sip-tracker
   ```

3. Build the project using Maven:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```


## Usage

Once the application is running, you can access the interface via `http://localhost:8080`. From there, you can add, manage, and analyze your SIP investments.


## Contributing
Contributions are welcome! Please feel free to submit a pull request or open an issue to discuss improvements or features.

## License
This project does not currently specify a license. Please check the repository for any updates regarding licensing.

## Contact
For any questions or feedback, please reach out to me "nt39142@gmail.com".




