#!/bin/bash
# Multicast Diagnostic Script

echo "=== Network Interfaces ==="
ifconfig | grep -E "^[a-z]|inet " | grep -A 1 "^en0\|^utun"

echo ""
echo "=== Multicast Routes ==="
netstat -rn | grep "224.0.0"

echo ""
echo "=== Java Network Properties ==="
java -XshowSettings:properties -version 2>&1 | grep -i "network\|interface"

echo ""
echo "=== Testing Multicast Send ==="
# Try to send a test multicast packet
echo "If this hangs, multicast is blocked..."

