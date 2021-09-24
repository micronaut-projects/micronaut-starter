$version = '3.0.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '88A9090EB1379E3870F00276AB6FFFEE2E1EE263F178372A4D79CEAF560F664A'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
