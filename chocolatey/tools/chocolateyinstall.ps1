$version = '5.0.0'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '859DBC4D539F0229DC6B6A7615F00F20536FA15B1A95146D2080CC310F63EEF9'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
