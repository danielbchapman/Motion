package com.danielbchapman.video;

class StreamProtocol
{
  static byte[] header = { 'm', 'o', 't', 'i', 'o', 'n' };

  static int data = 4; // integer length

  public static int decode(byte a, byte b, byte c, byte d)
  {
    int x = (a & 0xFF) << 24 | (b & 0xFF) << 16 | (c & 0xFF) << 8 | (d & 0xFF);
    // System.out.print("(" + a + ", ");
    // System.out.print(b + ", ");
    // System.out.print(c + ", ");
    // System.out.print(d + ") byte[] decode to " + x);
    // System.out.println();
    return x;
  }

  public static byte[] encodeBytes(byte[] bytes)
  {
    byte[] packet = new byte[header.length + data + bytes.length + header.length];

    for (int i = 0; i < header.length; i++)
    {
      packet[i] = header[i];
      packet[packet.length - i - 1] = header[i];
    }

    // encode length
    byte[] length = { (byte) (bytes.length >>> 24), (byte) (bytes.length >>> 16), (byte) (bytes.length >>> 8), (byte) (bytes.length >>> 0) };

    int k = 0;
    packet[header.length + k] = length[k++];
    packet[header.length + k] = length[k++];
    packet[header.length + k] = length[k++];
    packet[header.length + k] = length[k++];

    for (int i = 0; i < bytes.length; i++)
    {
      packet[header.length + data + i] = bytes[i];
    }

    // System.out.println("Here's a packet of length" + packet.length);
    // for(byte b : packet)
    // System.out.print(" " + (char) b);
    //
    // System.out.println();
    // for(byte b : packet)
    // System.out.print( " " + (int) b);
    // System.out.println();
    return packet;
  }

  public static byte[] decodeBytes(byte[] bytes)
  {
    if (bytes.length < (header.length + data + header.length))
      return null;

    // Checksum here...
    for (int i = 0; i < header.length; i++)
    {
      if (bytes[i] != header[i])
      {
        throw new RuntimeException("Packet data mismatch, header");
      }

      if (bytes[bytes.length - i - 1] != header[i])
      {
        String last6 = "";
        for (int z = 0; z < 6; z++)
        {
          last6 += (char) bytes[bytes.length - z - 1];
        }
        last6 = "Packet data mismatch, tail " + last6;
        throw new RuntimeException(last6);
      }

    }

    int k = 0;

    byte a = bytes[header.length + k++];
    byte b = bytes[header.length + k++];
    byte c = bytes[header.length + k++];
    byte d = bytes[header.length + k++];

    int length = (a & 0xFF) << 24 | (b & 0xFF) << 16 | (c & 0xFF) << 8 | (d & 0xFF);
    // System.out.println("length decode is -> " + length);

    byte[] result = new byte[length];

    // Checksum on length

    for (int i = 0; i < length; i++)
    {
      result[i] = bytes[i + header.length + data];
    }

    // System.out.println("Returning result:");
    // for(byte b : result)
    // System.out.print(" " + (char)b);
    // System.out.println();
    // for(byte b : result)
    // System.out.print(" " + (int)b);
    // System.out.println();
    return result;
  }
}
