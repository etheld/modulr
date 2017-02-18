Technical test, here is the description:

Modulr Java Technical Test
Create an interface AccountService which declares;
* Check balance
* Withdraw an amount

Create AccountServiceImpl that is aware of the following accounts and balances
* Account number 01001, Balance 2738.59
* Account number 01002, Balance 23.00
* Account number 01003, Balance 0.00

Create ATMServiceImpl and set it up to use the AccountServiceImpl. This should have the following
behaviour;
* Replenish:
  * Sets up the service with currency notes of denominations 5, 10, 20 and 50
* Check balance:
  * Returns a formatted string to display
* Withdraw:
  * Returns notes of the correct denominations
  * Allow withdrawals between 20 and 250 inclusive, in multiples of 5
  * Disburse smallest number of notes
  * Always disburse at least one 5 note, if possible

Assume currency as GBP. It is acceptable to disregard currency for all operations.

Please submit;
1. A Java project with appropriate classes to solve the problem above
2. Unit test cases to prove the functionality works as expected
3. Appropriate use of a logging framework of your choice
4. Any assumptions that you may have made 
