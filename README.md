# PGP (Pretty Good Privacy) for Android

### What is PGP?
Pretty Good Privacy (PGP) is an encryption system used for both sending encrypted emails and encrypting sensitive files.

### What is PGP for Android?
This application encrypts files using PGP protocol for secure transmission of files between two people.

### Installation:

- Download the "PGP-Tool.apk" from [here](https://github.com/Porvil/PGP-Tool/releases).
- Install the APK.

### Usage:

- Give Read/Write Permissions.

***The Receiver and Sender Key should be of same size and same set of keys must be used for encryption and decryption.***

##### Sender (Encrypt File): 
1) Generate Key
2) Go to "View Keys" page, and send public key of the key which is generated in step 1 and send it to Reciever.
3) Ask Reciever to send his/her public key.
4) Go to "Add Other's Keys" page, and browse and select reciever's key.
5) Go to "Encrypt File" page.
6) Select file to encrypt.
7) Select your key (Sender's key) and enter key's password.
8) Select other's key (Reciever's key).
9) Enter filename to save the encrypted file.
10) Press "Encrypt".

> The encrypted file would be saved in "internal storage/PGP Tool/Encrypted/*filename.pgpdata*".

> You can send the encrypted file by using any file manager by going to "internal storage/PGP Tool/Encrypted/*filename.pgpdata*".

##### Reciever (Decrypt File):
1) Generate Key
2) Go to "View Keys" page, and send public key of the key which is generated in step 1 to sender.
3) Ask Sender to send his/her public key.
4) Go to "Add Other's Keys" page, and browse and select sender's key.
5) Go to "Decrypt File" page.
6) Select file to decrypt.
7) Select your key (Reciever's key) and enter key's password.
8) Select other's key (Sender's key).
9) Press "Decrypt".

>The decrypted file would be saved in "internal storage/PGP Tool/Decrypted/*originalfilename.extension*"


##### Generating Key:
- Select Key Size (available sizes - 1024, 2048, 4096)
- Enter Key Owner name.
- Enter Key Name.
- Enter Password.

> Key will be generated using the above details in "internal storage/PGP Tool/My Keys/*keyname.key*"

> You can send public key by going to "View Keys" page by selecting the key you want to send.
You can also change the password of your keys by again going to "View Keys" page.


### Algorithms Used: 
- AES with CBC and PKCS5Padding (For encrypting data)
- RSA with ECB and PKCS1Padding (For encrypting AES session Key)
- SHA1 with RSA (For Digital Signature)
- PBKDF2 with HmacSHA1 (For Hash used in password for private keys)

### Supported Android Versions:
- Android 5.0 (API Level-21) and above.

### Directories Documentation: 

- PGP Directory - **"internal storage/PGP Tool/"**
- My Keys Directory - **"internal storage/PGP Tool/My Keys/"**
- Others Keys Directory - **"internal storage/PGP Tool/Others Keys/"**
- Encrypted Files Directory - **"internal storage/PGP Tool/Encrypted/"**
- Decrypted Files Directory - **"internal storage/PGP Tool/Decrypted/"**

### Mentions: 
- [S.A.Parkhid](https://stackoverflow.com/users/568822/s-a-parkhid) for his/her answer on https://stackoverflow.com/questions/13209494/how-to-get-the-full-file-path-from-uri/60642994#answer-60642994 
- "FilesUtilsMine.java" is taken from the above link.

### License:
----

MIT License

Copyright (c) 2020 Porvil

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
----
