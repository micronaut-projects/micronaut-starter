$version = '5.2.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '7288C784FAFD4247F40704010C00DEF4CF249E49872E61A41EFC12AC753CBD7B'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
