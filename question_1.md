# Create a virtualisation environment using KVM/Xen/HyperV/Virtualbox. 
# Setup a network of two or more virtual machines.

## Checking support for KVM
```bash
$ LC_ALL=C lscpu | grep Virtualization
```

Alternatively:

```bash
$ grep -E --color=auto 'vmx|svm|0xc0f' /proc/cpuinfo
```

If nothing is displayed after running either command, then your processor does
 not support hardware virtualization,nand you will not be able to use KVM.

> Note: You may need to enable virtualization support in your BIOS. All 
> x86_64 processors manufactured by AMD and Intel in the last 10 years support
>  virtualization. If it looks like your processor does not support 
> virtualization, it's almost certainly turned off in the BIOS.

### Steps for enabling Virtualization(varies by vendor, example follows an ASUS
### laptop which I currently use)
- Press ```F2``` when powering on.
- Press ```F7``` to enter Advanced Mode.
- Navigate to ```Advanced``` tab.
- Expand ```CPU Configuration```.
- Enable ```SVM```.
- Press ```F10``` to save & exit.

## Kernel Support
I use Manjaro Linux, a derivative of Arch Linux which provides the necessary 
modules to support KVM, this applies 
to Debian and RHEL based distributions as well.

- One can check if the necessary modules, ```kvm``` and either ```kvm_amd``` or
-  ```kvm_intel```, are available in
   the kernel with the following command:

```bash
$ zgrep CONFIG_KVM /proc/config.gz
```

The module is available if all the configuration parameters are set to either
 ```y``` or ```m```.

- Then ensure the kernel modules are loaded with the following command:
```bash
$ lsmod | grep kvm
```

## How to use KVM
To use KVM, we need an emulator. We use QEMU.

### Installation
> Installation was performed on Manjaro Linux running ```5.11.10-1-MANJARO```

Install virt-manager, qemu and all dependencies:

```bash
pacman -S virt-manager qemu vde2 ebtables dnsmasq bridge-utils openbsd-netcat
```

Enable and start the service:

```bash
systemctl enable libvirtd.service
systemctl start libvirtd.service
```


### Usage
- Launch ```virt-manager```
  #### Creating a VM:
  - Choose ```New Virtual Machine``` from the ```File``` menu.
  - Choose the installation media type.
  - Browse for the installation media.
  - Allocate CPU and Memory to the VM.
  - Enable storage and choose the size of disk image for the VM.
  - On the last page, review your settings. Hit ```Finish``` when ready.

The VM will be provisioned and started with the installation media. You may 
then proceed with OS installation.

#### Post-installation
```bash
# Enable ssh and getty services
systemctl enable --now sshd
systemctl enable --now getty@ttyS0
```

### Useful commands
```bash
# commands to start, shutdown a VM, list network details, access the 
# terminal(tty), list all VMs given in order
virsh start vm_name
virsh shutdown vm_name
virsh domifaddr vm_name
virsh console vm_name
virsh list --all
```
