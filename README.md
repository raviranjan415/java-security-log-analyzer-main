# Java Security Log Analyzer (SOC Tool)

## Overview
This project simulates a Security Operations Center (SOC) log analysis tool built in Java. It detects brute-force login attempts and identifies attacker IP addresses using behavioral and time-based analysis.

## Features
- Detects failed login attempts
- Tracks suspicious activity per user and IP
- Identifies brute-force attack patterns
- Time-based attack detection (burst analysis)
- Simulates firewall auto-blocking of malicious IPs
- Generates security reports

## Technologies
- Java
- File I/O
- HashMap (data aggregation)
- Date/Time API

## Example Output
🚨 ALERT: Brute-force attack detected  
🚫 BLOCKED: 192.168.1.10  

## Use Case
This tool demonstrates how SIEM platforms like Splunk analyze logs and detect threats in real-world cybersecurity environments.

## Author
Omar – Cybersecurity & Digital Forensics