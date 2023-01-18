# AuctionHouse
Change the IP in the server main to your machines IP to make sure the server runs correctly. When running the client you will first be prompted to enter the servers ip.
1. To bid on something you need money in your account. To do that, enter the number corresponding to "Deposit Funds". Here you enter how much money you want to deposit.
INTEGER VALUES ONLY!
2. To get an auction going you enter in the lobby the number corresponding to "Create new listing". Here you enter an item as follows: Example 1234
3. To bid on an auction, enter the number corresponding to "View auction listings" and then the number of the auction you want to bid on.
To bid on something, simply type how much you want to bid.
4. If you win an auction, you can check the items you have in your inventory by selecting that option in the lobby menu.

Unfortunately there are some errors that we couldn't resolve.
1. There is no validation of inputs, so if you input something invalid the program will simply crash.
2. When an auction you have bid on is finished, whether it is a single one or multiple, the lobby requires a few inputs before it functions as normal.
This was simply due to inexperience with running parallel threads. Some methods don't end correctly when returning to the lobby.

A different type of UI, one more strict, would perhaps make these problems go away, but as for the limited time of this project we weren't able to implement that.
