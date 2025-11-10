#!/bin/bash
# FINAL FIX for VPN blocking multicast

echo "=========================================="
echo "Fixing Multicast Routing with VPN Active"
echo "=========================================="
echo ""

# Get the IP address of en0
EN0_IP=$(ifconfig en0 | grep "inet " | awk '{print $2}')

if [ -z "$EN0_IP" ]; then
    echo "ERROR: Could not find IP address for en0"
    echo "Make sure you're connected to Wi-Fi"
    exit 1
fi

echo "Found en0 IP: $EN0_IP"
echo ""

# Step 1: Delete any existing multicast routes
echo "Step 1: Cleaning up existing multicast routes..."
sudo route delete -net 224.0.0.0/4 2>/dev/null
sudo route delete -net 224.0.0.0/4 -interface en0 2>/dev/null
sudo route delete -net 224.0.0.0/4 -interface utun4 2>/dev/null
echo "  Done"
echo ""

# Step 2: Add multicast route via en0's gateway
echo "Step 2: Adding multicast route via en0 ($EN0_IP)..."
sudo route add -net 224.0.0.0/4 $EN0_IP
echo "  Done"
echo ""

# Step 3: Verify the route
echo "Step 3: Verifying multicast route for 228.5.6.7..."
echo "-----------------------------------------------"
route -n get 228.5.6.7 | grep -E "interface|gateway"
echo "-----------------------------------------------"
echo ""

# Check if it's using en0
INTERFACE=$(route -n get 228.5.6.7 | grep "interface:" | awk '{print $2}')

if [ "$INTERFACE" = "en0" ]; then
    echo "✅ SUCCESS! Multicast will now use en0 (Wi-Fi)"
    echo ""
    echo "You can now run:"
    echo "  Terminal 1: java MulticastServer"
    echo "  Terminal 2: java MulticastClient"
else
    echo "⚠️  WARNING: Multicast is still using $INTERFACE"
    echo ""
    echo "This might be a persistent VPN issue."
    echo "You may need to temporarily disconnect the VPN."
fi

echo ""
echo "Current multicast routes:"
netstat -rn | grep "224.0.0"

