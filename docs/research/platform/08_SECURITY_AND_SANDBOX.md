# 08 - Security And Sandbox

## Threat Model

Online judge se chay arbitrary code. Rui ro:

- Arbitrary code execution tren host.
- Fork bomb/process spawning.
- Filesystem access.
- Network access/outbound scan.
- Memory exhaustion.
- CPU abuse.
- Container escape.
- Data exfiltration.
- Malicious serialized objects.
- Dependency download.
- Compile-time abuse.
- Infinite blocking socket/read.

## Controls Bat Buoc Cho Online Judge

- Container per submission.
- No outbound network; chi cho phep ket noi toi mock server noi bo cua submission neu can.
- Read-only filesystem cho image/toolchain.
- Ephemeral writable workspace.
- CPU limit bang cgroup.
- Memory limit bang cgroup.
- Process limit/pids limit.
- Wall-clock timeout.
- Seccomp/AppArmor profile.
- Drop capabilities.
- Non-root user trong container.
- No Docker socket mount.
- Queue worker tach khoi web API.
- Cleanup workspace/container sau run.
- Log gioi han kich thuoc.

## Java-specific Risks

- `Runtime.exec`, process spawn.
- Reflection/classloader abuse.
- Serialization gadget/object bomb.
- Infinite thread creation.
- Large allocation.

Control:

- Process limit.
- Memory limit.
- No outbound net.
- Validate serialized object trong mock server; khong deserialize untrusted object ngoai sandbox neu khong can.
- Chay mock server trong cung sandbox hoac network namespace rieng.

## MVP Security Boundary

Phase 1 khong co rui ro arbitrary code.

Phase 2 local judge: rui ro nam o may nguoi hoc, khong phai server. Van can canh bao script chi chay local.

Phase 3 moi can sandbox production. Khong duoc tu viet sandbox so sai. Nen dung Docker/Podman voi hardening, hoac giai phap judge sandbox da duoc audit neu co.

## Nhung Thu Khong Duoc Lam So Sai

- Chay code submit truc tiep tren host.
- Cho container co Internet outbound tu do.
- Mount source/server secret vao sandbox.
- Dung chung workspace giua submissions.
- Tin vao Java SecurityManager cu.
- Cho npm/pip/maven download dependency tu Internet trong run.
- Expose Docker socket cho worker code.

