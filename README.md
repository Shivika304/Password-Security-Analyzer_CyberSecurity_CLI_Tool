I have created a Password Security Analyzer (CyberSecurity CLI Tool), which checks the following things in a password:
	1.	Password Strength Detection – Analyzes the password based on length, uppercase letters, lowercase letters, numbers, and special characters to determine its strength.
	2.	Dictionary Attack Detection – Checks whether the password exists in the RockYou leaked password dataset, which indicates it can be easily cracked using dictionary attacks.
	3.	Entropy Calculation – Calculates password entropy to measure how unpredictable and secure the password is.
	4.	Crack Time Estimation – Estimates how long it would take for an attacker to crack the password assuming a high-speed brute-force attack.
	5.	Sequential Pattern Detection – Detects predictable sequences such as 1234 or abcd that reduce password security.
	6.	Keyboard Pattern Detection – Identifies keyboard patterns like qwerty, asdfgh, or zxcvbn which are commonly used weak passwords.
	7.	Repeated Character Detection – Flags passwords containing repeated characters like aaaaaa or 111111.
	8.	Leetspeak Detection – Detects passwords that disguise common words using character substitutions like p@ssw0rd.
	9.	Security Suggestions – Provides recommendations to improve weak passwords, such as increasing length or adding character diversity.
