package com.wesleyarjunun.ipsubnetcalculator;

public class IPv4SubnetCalculator {
    public SubnetResult calculate(String input) {
        if (input == null || input.isBlank())
            throw new IllegalArgumentException("Enter an IPv4 address and prefix, for example 192.168.1.10/24.");
        String[] parts = input.trim().split("/", -1);
        if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank())
            throw new IllegalArgumentException("Use CIDR notation, for example 192.168.1.10/24.");

        long ip = parseIPv4(parts[0].trim());
        int prefix;
        try { prefix = Integer.parseInt(parts[1].trim()); }
        catch (NumberFormatException ex) {
            throw new IllegalArgumentException("The CIDR prefix must be a number from 0 to 32.");
        }
        if (prefix < 0 || prefix > 32)
            throw new IllegalArgumentException("The CIDR prefix must be between 0 and 32.");

        long mask = prefix == 0 ? 0L : (0xFFFFFFFFL << (32 - prefix)) & 0xFFFFFFFFL;
        long wildcard = (~mask) & 0xFFFFFFFFL;
        long network = ip & mask;
        long broadcast = network | wildcard;
        long total = 1L << (32 - prefix);

        long first, last, usable;
        if (prefix <= 30) {
            first = network + 1; last = broadcast - 1; usable = total - 2;
        } else if (prefix == 31) {
            first = network; last = broadcast; usable = 2;
        } else {
            first = network; last = network; usable = 1;
        }

        return new SubnetResult(parts[0].trim() + "/" + prefix, prefix, toIPv4(mask),
                toIPv4(wildcard), toIPv4(network), toIPv4(broadcast),
                toIPv4(first), toIPv4(last), total, usable);
    }

    private long parseIPv4(String address) {
        String[] octets = address.split("\\.", -1);
        if (octets.length != 4)
            throw new IllegalArgumentException("Enter a valid IPv4 address with four octets.");
        long value = 0;
        for (String octet : octets) {
            if (octet.isEmpty() || !octet.matches("\\d{1,3}"))
                throw new IllegalArgumentException("Each IPv4 octet must be a number from 0 to 255.");
            int number = Integer.parseInt(octet);
            if (number > 255)
                throw new IllegalArgumentException("Each IPv4 octet must be between 0 and 255.");
            value = (value << 8) | number;
        }
        return value;
    }

    private String toIPv4(long value) {
        return ((value >>> 24) & 255) + "." + ((value >>> 16) & 255) + "."
                + ((value >>> 8) & 255) + "." + (value & 255);
    }

    public record SubnetResult(String input, int prefix, String subnetMask, String wildcardMask,
            String networkAddress, String broadcastAddress, String firstUsableHost,
            String lastUsableHost, long totalAddresses, long usableHosts) {
        public String toReport() {
            return "IP & Subnet Calculator Report\n"
                    + "Input: " + input + "\nCIDR prefix: /" + prefix
                    + "\nSubnet mask: " + subnetMask + "\nWildcard mask: " + wildcardMask
                    + "\nNetwork address: " + networkAddress + "\nBroadcast address: " + broadcastAddress
                    + "\nFirst usable host: " + firstUsableHost + "\nLast usable host: " + lastUsableHost
                    + "\nTotal addresses: " + totalAddresses + "\nUsable hosts: " + usableHosts + "\n";
        }
    }
}
