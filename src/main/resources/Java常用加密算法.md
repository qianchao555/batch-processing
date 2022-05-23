## Java常用加密算法

### Base 64

用于网络中传输的数据进行编码，严格意义上属于编码的格式，有64个字符的对应的编码，Base64就是将内容按照该格式进行编码。可以对数据编码和解码，是可逆的，安全度较低

Base64可以使用JDK中自带的类实现，还可以使用Bouncy Castle或Commons Codec实现

~~~java
public class Test {

    public static void main(String[] args) {
        // 原始数据
        String str = "Hello World";

        // 加密
        Base64.Encoder encoder = Base64.getEncoder();
        String encoderStr = encoder.encodeToString(str.getBytes("utf-8"));
        System.out.println("encoder: " + encoderStr);

        // 解密
        Base64.Decoder decoder = Base64.getDecoder();
        String decoderStr = new String(decoder.decode(encoderStr), "utf-8");
        System.out.println("decoder: " + decoderStr);
    }
}

~~~



### 摘要算法

摘要算法主要用于校验数据完整性，当我们在下载某些文件时，会有MD5和SHA1值提供校验下载的文件是否完整，可以用于根据数据生成其唯一的摘要值，无法根据摘要值直到原数据，属于不可逆的

#### MD

JDK有MD2和MD5的实现，使用的是MessageDigest类，两者摘要长度均为128

#### SHA

SHA1、224、2561、-384、-512

#### Hmac

### 对称加密

严格意义上的加密算法，分为对称和非对称加密算法，所谓对称指的是发送方和接收方的密钥是一样的。因为密钥一样，所以安全性较非对称而言就不太安全

对称加密主要有：DES、3DES(3重DES)、AES、PBE(基于口令的对称算法)

#### DES

#### AES

#### PBE

### 非对称加密

发送方和接收方的密钥不一致，有一个公钥和私钥的概念，公钥是公开的，比如在网络上传输，而私钥安全性要求较高，因为私钥是保密的

主要非对称算法有：DH、RSA、ELGamal

#### DH

基于交换的非对称算法，发送方需要得到接收方的key构建本地密钥，而接收方也需要得到发送方的key构建自己本地的密钥

#### RSA

相比于DH算法的实现更加简单，适用范围广，公钥和私钥的创建较简单，而且支持公钥加密、私钥解密或私钥加密、公钥解密两者方式

keyGenerator，keyPairGenerator，SecretKeyFactory的区别

---

### 加密盐

盐就是一个随机字符串用来和我们的加密串拼接后进行加密

加盐主要是为了提供加密字符串的安全性。假如有一个加盐后的加密串，黑客通过一定手段得到这个加密串，他拿到的明文，并不是我们加密前的字符串，而是加密前的字符串和盐组合的字符串，这样相对来说又增加了字符串的安全性