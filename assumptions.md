
AtmService:
The spec was not specific about whether the ATM service should keep record of banknotes in the ATM, so I was implementing it as in a real world ATM machine should work
Only one user can use the service at a time so thready safety was not a concern
I used exceptions for exceptional cases/error handling, although it was not meant to be used directly by the end user.
Added java docs for functions, explaining exceptions and return types

AccountService:
checkBalance takes integer as account number, which does not preserve the leading zeros, but it is just for sending parameter to the backend, not to display it


General:
There was no time limit, so I tried to deliver the functionality as I think it is right
