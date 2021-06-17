$version = '3.0.0-M2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '80E2C7EFB202AE1F3D5B7117134D2825B7EF319B1C700E96EFB29FCA54C03CEB'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
